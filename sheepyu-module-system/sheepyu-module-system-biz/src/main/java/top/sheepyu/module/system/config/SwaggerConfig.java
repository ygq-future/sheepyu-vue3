package top.sheepyu.module.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import top.sheepyu.framework.swagger.factory.DocketFactory;

/**
 * @author ygq
 * @date 2024-07-12 11:21
 **/
@Configuration
public class SwaggerConfig {
    public final String groupName = "system";

    @Bean
    public Docket systemDocket(DocketFactory docketFactory) {
        return docketFactory.buildDocket(groupName);
    }
}
