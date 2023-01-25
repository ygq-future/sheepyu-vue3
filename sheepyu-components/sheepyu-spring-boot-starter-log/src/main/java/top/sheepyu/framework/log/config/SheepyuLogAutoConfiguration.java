package top.sheepyu.framework.log.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.sheepyu.framework.log.core.aop.RecordLogAspect;
import top.sheepyu.framework.log.core.service.ApiLogFrameworkService;
import top.sheepyu.framework.log.core.service.ApiLogFrameworkServiceImpl;

/**
 * @author ygq
 * @date 2023-01-17 16:13
 **/
@Configuration
@EnableConfigurationProperties({ApiLogProperties.class})
public class SheepyuLogAutoConfiguration {
    @Bean
    public RecordLogAspect apiLogAspect() {
        return new RecordLogAspect();
    }

    @Bean
    public ApiLogFrameworkService apiLogFrameworkService() {
        return new ApiLogFrameworkServiceImpl();
    }
}
