package top.sheepyu.framework.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ygq
 * @date 2023-01-22 12:19
 **/
@ConfigurationProperties(prefix = "sheepyu.sms")
@Data
public class SmsProperties {
    private boolean mailEnable = true;
}
