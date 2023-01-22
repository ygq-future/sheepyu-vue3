package top.sheepyu.framework.sms.core.sender;

import top.sheepyu.framework.sms.core.enums.SmsTypeEnum;
import top.sheepyu.framework.sms.core.params.SenderParams;

/**
 * @author ygq
 * @date 2023-01-22 16:36
 **/
public class DefaultSmsSender implements SmsSender {
    @Override
    public void sendCode(SenderParams params) {

    }

    @Override
    public boolean verifyCode(String key, String code) {
        return false;
    }

    @Override
    public String getType() {
        return SmsTypeEnum.DEFAULT.getCode();
    }
}
