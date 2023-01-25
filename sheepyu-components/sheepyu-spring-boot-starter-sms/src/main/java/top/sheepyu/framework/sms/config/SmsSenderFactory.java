package top.sheepyu.framework.sms.config;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.sheepyu.framework.sms.core.enums.SmsSenderTypeEnum;
import top.sheepyu.framework.sms.core.sender.SmsSender;
import top.sheepyu.module.system.api.ConfigApi;
import top.sheepyu.module.system.enums.SystemConfigKeyEnum;

import javax.annotation.Resource;
import java.util.Map;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.SMS_SENDER_DONT_MATCH;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-25 17:15
 **/
public class SmsSenderFactory implements ApplicationContextAware, SmartInitializingSingleton {
    @Resource
    private ConfigApi configApi;
    private ApplicationContext applicationContext;
    private static final Map<String, SmsSender> SMS_SENDER_MAP = new HashedMap<>();

    @Override
    public void afterSingletonsInstantiated() {
        init();
    }

    public void init() {
        Map<String, SmsSender> smsSenderMap = applicationContext.getBeansOfType(SmsSender.class);
        if (CollUtil.isNotEmpty(smsSenderMap)) {
            smsSenderMap.forEach((k, v) -> SMS_SENDER_MAP.put(v.getType(), v));
        }
    }

    public SmsSender get() {
        String type = configApi.get(SystemConfigKeyEnum.DEFAULT_SMS_SENDER);
        return get(SmsSenderTypeEnum.value(type));
    }

    public SmsSender get(SmsSenderTypeEnum type) {
        SmsSender smsSender = SMS_SENDER_MAP.get(type.getCode());
        if (smsSender == null) {
            throw exception(SMS_SENDER_DONT_MATCH);
        }
        return smsSender;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
