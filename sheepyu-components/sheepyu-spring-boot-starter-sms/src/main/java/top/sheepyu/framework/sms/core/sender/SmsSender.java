package top.sheepyu.framework.sms.core.sender;

import top.sheepyu.framework.sms.core.params.SenderParams;

/**
 * 后面如果需要扩展功能, 比如说用邮件发送一个啥啥啥通知
 * 那么直接在接口新增方法比如sendInformation, 然后实现类实现即可
 *
 * @author ygq
 * @date 2023-01-22 12:23
 **/
public interface SmsSender {
    /**
     * 发送验证码
     *
     * @param params 某种发送类型所需要的参数, 比如邮件发送可能要收件方等一些信息
     * @return 返回存入redis的key
     */
    void sendCode(SenderParams params);

    /**
     * 校验验证码
     *
     * @param key  redis中的key
     * @param code 验证码
     */
    boolean verifyCode(String key, String code);

    /**
     * 返回当前是什么类型的Sender, 和SmsProperties中的type搭配使用
     *
     * @return 返回类型
     */
    String getType();
}
