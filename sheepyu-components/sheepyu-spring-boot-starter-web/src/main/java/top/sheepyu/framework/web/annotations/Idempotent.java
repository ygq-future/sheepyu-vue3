package top.sheepyu.framework.web.annotations;

import top.sheepyu.framework.web.enums.KeyPrefixTypeEnum;
import top.sheepyu.framework.web.resolver.DefaultProtectionKeyResolver;
import top.sheepyu.framework.web.resolver.ProtectionKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 保证接口幂等
 *
 * @author ygq
 * @date 2023-01-20 15:10
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * 幂等的超时时间，默认为 1 秒
     * <p>
     * 注意，如果执行时间超过它，请求还是会进来
     */
    int timeout() default 1;

    /**
     * 时间单位，默认为 SECONDS 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * redis的key前缀基于什么, 默认时基于用户id
     * 也就是最终的key是keyPrefix + keyResolver()
     * 因为有些场景是针对于用户的幂等操作, 有些场景是针对于全局的幂等
     * 注意如果是基于用户模式, 那么一定要标注在需要用户登录的接口上(要保证调用接口时用户是登录状态)
     */
    KeyPrefixTypeEnum keyPrefixType() default KeyPrefixTypeEnum.USER;

    /**
     * 解析key来作为redis中的key, 一定要能够唯一确认一个接口
     * 默认实现DefaultProtectionKeyResolver
     */
    Class<? extends ProtectionKeyResolver> keyResolver() default DefaultProtectionKeyResolver.class;
}
