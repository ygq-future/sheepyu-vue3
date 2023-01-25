package top.sheepyu.framework.web.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.web.core.annotations.Idempotent;
import top.sheepyu.framework.web.core.resolver.ProtectionKeyResolver;

import javax.annotation.Resource;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.REPEATEDLY_REQUEST;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-20 15:44
 **/
@Aspect
public class IdempotentAspect {
    @Resource
    private RedisUtil redisUtil;
    private static final String IDEMPOTENT_KEY = "protection:idempotent:";

    @Before("@annotation(idempotent)")
    public void before(JoinPoint jp, Idempotent idempotent) throws Exception {
        Class<? extends ProtectionKeyResolver> clazz = idempotent.keyResolver();
        ProtectionKeyResolver keyResolver = clazz.getDeclaredConstructor().newInstance();

        String key = keyResolver.resolver(jp).concat(idempotent.keyPrefixType().getPrefix());
        key = IDEMPOTENT_KEY.concat(key);
        if (!redisUtil.setNx(key, idempotent.timeout(), idempotent.timeUnit())) {
            throw exception(REPEATEDLY_REQUEST);
        }
    }
}
