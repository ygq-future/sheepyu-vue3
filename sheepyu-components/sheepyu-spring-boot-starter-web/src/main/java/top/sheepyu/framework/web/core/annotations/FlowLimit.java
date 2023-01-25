package top.sheepyu.framework.web.core.annotations;

import top.sheepyu.framework.web.core.enums.KeyPrefixTypeEnum;
import top.sheepyu.framework.web.core.resolver.DefaultProtectionKeyResolver;
import top.sheepyu.framework.web.core.resolver.ProtectionKeyResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流
 * 默认值一分钟十次
 *
 * @author ygq
 * @date 2023-01-20 15:08
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowLimit {
    /**
     * 限流的次数
     */
    int value() default 10;

    /**
     * 多少时间内
     */
    int timeout() default 1;

    /**
     * 代表time的单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * redis的key前缀基于什么, 默认时基于客户端IP
     * 也就是最终的key是keyPrefix + keyResolver()
     * 因为有些场景是针对于用户的限流操作, 有些场景是针对于ip的限流
     */
    KeyPrefixTypeEnum keyPrefixType() default KeyPrefixTypeEnum.IP;

    /**
     * 解析key来作为redis中的key, 一定要能够唯一确认一个接口
     * 默认实现DefaultProtectionKeyResolver
     */
    Class<? extends ProtectionKeyResolver> keyResolver() default DefaultProtectionKeyResolver.class;
}
