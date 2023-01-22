package top.sheepyu.framework.sms.core.constants;

import top.sheepyu.framework.sms.core.sender.DefaultSmsSender;
import top.sheepyu.framework.sms.core.sender.EmailSmsSender;
import top.sheepyu.framework.sms.core.sender.SmsSender;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-22 15:31
 **/
public interface SmsSenderConstants {
    /**
     * redis key前缀
     */
    String SMS_CODE_KEY = "sms:code:";
    long SMS_CODE_TTL = 2;

    /**
     * 所有的实现类
     */
    List<Class<? extends SmsSender>> CHILDREN = List.of(
            DefaultSmsSender.class,
            EmailSmsSender.class
    );
}
