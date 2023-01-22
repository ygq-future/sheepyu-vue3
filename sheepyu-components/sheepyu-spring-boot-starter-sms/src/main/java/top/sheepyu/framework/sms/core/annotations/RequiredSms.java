package top.sheepyu.framework.sms.core.annotations;

import top.sheepyu.framework.sms.core.enums.SmsTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ygq
 * @date 2023-01-22 15:11
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredSms {
    SmsTypeEnum type() default SmsTypeEnum.EMAIL;
}
