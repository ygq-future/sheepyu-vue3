package top.sheepyu.framework.sms.core.sender.email;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.sms.core.enums.SmsSenderTypeEnum;
import top.sheepyu.framework.sms.core.sender.SenderParams;
import top.sheepyu.framework.sms.core.sender.SmsSender;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ygq
 * @date 2023-01-22 12:24
 **/
@Slf4j
@Component
@ConditionalOnProperty(prefix = "sheepyu.sms", value = "mailEnable")
public class EmailSmsSender implements SmsSender {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private RedisUtil redisUtil;
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendCode(SenderParams params) {
        if (!params.getClass().isAssignableFrom(EmailParams.class)) {
            throw new IllegalArgumentException("参数应该是EmailParams!");
        }
        EmailParams emailParams = (EmailParams) params;
        SimpleMailMessage message = new SimpleMailMessage();

        String receiveEmail = emailParams.getReceiveEmail();
        String code = RandomUtil.randomNumbers(emailParams.getCodeLength());
        String content = String.format("您的验证码是: %s, 该验证码会在两分钟后失效, 请勿泄露! 如果您不清楚为什么有这封邮件, 那么忽略即可.", code);

        message.setFrom(senderEmail);
        message.setTo(receiveEmail);
        message.setSubject("来自洋芋系统的验证码通知");
        message.setText(content);

        try {
            javaMailSender.send(message);
            redisUtil.set(SMS_CODE_KEY.concat(receiveEmail), code, SMS_CODE_TTL, TimeUnit.MINUTES);
        } catch (MailException e) {
            log.error("邮件发送失败, 发送人: {}, 接收人: {}, 内容: {}", senderEmail, receiveEmail, content);
        }
    }

    @Override
    public boolean verifyCode(String key, String code) {
        if (StrUtil.hasBlank(key, code)) {
            return false;
        }

        String redisKey = SMS_CODE_KEY.concat(key);
        String redisCode = redisUtil.get(redisKey);
        redisUtil.del(redisKey);
        return Objects.equals(redisCode, code);
    }

    @Override
    public String getType() {
        return SmsSenderTypeEnum.EMAIL.getCode();
    }
}
