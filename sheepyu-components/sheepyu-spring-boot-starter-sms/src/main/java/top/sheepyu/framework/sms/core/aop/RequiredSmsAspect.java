package top.sheepyu.framework.sms.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import top.sheepyu.framework.sms.config.SmsProperties;
import top.sheepyu.framework.sms.core.annotations.RequiredSms;
import top.sheepyu.module.common.constants.ErrorCodeConstants;

import javax.annotation.Resource;
import java.util.Objects;

import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-22 15:11
 **/
@Aspect
public class RequiredSmsAspect {
    @Resource
    private SmsProperties smsProperties;

    @Before("@annotation(requiredSms)")
    public void before(RequiredSms requiredSms) {
        if (!smsProperties.isEnable()) {
            throw exception(ErrorCodeConstants.SMS_SENDER_DONT_MATCH);
        }

        if (!Objects.equals(requiredSms.type().getCode(), smsProperties.getDefaultType())) {
            throw exception(ErrorCodeConstants.SMS_SENDER_DONT_MATCH);
        }
    }
}
