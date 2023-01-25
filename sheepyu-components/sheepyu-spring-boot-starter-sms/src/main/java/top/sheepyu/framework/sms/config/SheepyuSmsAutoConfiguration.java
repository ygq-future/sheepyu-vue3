package top.sheepyu.framework.sms.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ygq
 * @date 2023-01-22 12:18
 **/
@Configuration
@EnableConfigurationProperties({SmsProperties.class})
@ComponentScan({"top.sheepyu.framework.sms.core.sender"})
public class SheepyuSmsAutoConfiguration {
    @Bean
    public SmsSenderFactory smsSenderFactory() {
        return new SmsSenderFactory();
    }
}
