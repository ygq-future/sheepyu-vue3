package top.sheepyu.framework.web.aop;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.web.annotations.FlowLimit;
import top.sheepyu.framework.web.resolver.ProtectionKeyResolver;

import javax.annotation.Resource;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.ACCESS_FREQUENTLY;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-20 15:11
 **/
@Aspect
public class FlowLimitAspect {
    @Resource
    private RedisUtil redisUtil;
    private static final String FLOWLIMIT_KEY = "protection:flowlimit:";

    @Before("@annotation(flowLimit)")
    public void before(JoinPoint jp, FlowLimit flowLimit) throws Exception {
        Class<? extends ProtectionKeyResolver> clazz = flowLimit.keyResolver();
        ProtectionKeyResolver keyResolver = clazz.getDeclaredConstructor().newInstance();

        String key = keyResolver.resolver(jp).concat(flowLimit.keyPrefixType().getPrefix());
        key = FLOWLIMIT_KEY.concat(key);
        String countStr = redisUtil.get(key);
        if (StrUtil.isBlank(countStr)) {
            redisUtil.set(key, 1, flowLimit.timeout(), flowLimit.timeUnit());
            return;
        }

        int currCount = Integer.parseInt(countStr);
        if (currCount > flowLimit.value()) {
            throw exception(ACCESS_FREQUENTLY);
        }
        redisUtil.inr(key);
    }
}
