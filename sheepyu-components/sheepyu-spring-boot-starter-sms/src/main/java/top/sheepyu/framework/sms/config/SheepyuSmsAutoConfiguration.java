package top.sheepyu.framework.sms.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.sheepyu.framework.sms.core.aop.RequiredSmsAspect;
import top.sheepyu.framework.sms.core.enums.SmsTypeEnum;
import top.sheepyu.framework.sms.core.sender.SmsSender;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static top.sheepyu.framework.sms.core.constants.SmsSenderConstants.CHILDREN;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.SMS_SENDER_DONT_MATCH;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-22 12:18
 **/
@Configuration
@EnableConfigurationProperties({SmsProperties.class})
public class SheepyuSmsAutoConfiguration {
    @Resource
    private SmsProperties smsProperties;
    private static final Map<String, SmsSender> SMS_SENDER_MAP = new HashMap<>();

    public SheepyuSmsAutoConfiguration() throws Exception {
        for (Class<? extends SmsSender> clazz : CHILDREN) {
            SmsSender smsSender = clazz.getDeclaredConstructor().newInstance();
            SMS_SENDER_MAP.put(smsSender.getType(), smsSender);
        }
    }

    @Bean
    public SmsSender smsSender() {
        SmsSender smsSender;
        if (!smsProperties.isEnable()) {
            smsSender = SMS_SENDER_MAP.get(SmsTypeEnum.DEFAULT.getCode());
        } else {
            smsSender = SMS_SENDER_MAP.get(smsProperties.getDefaultType());
        }

        if (smsSender == null) {
            throw exception(SMS_SENDER_DONT_MATCH);
        }
        return smsSender;
    }

    @Bean
    public RequiredSmsAspect requiredSmsAspect() {
        return new RequiredSmsAspect();
    }
}
