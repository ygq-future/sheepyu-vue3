package top.sheepyu.framework.swagger.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.sheepyu.framework.swagger.factory.DocketFactory;

/**
 * 这里整合和knife4j的增强功能, 只要sheepyu.swagger.enable = true
 * 那么默认就开启了knife4j的增强功能, sheepyu.swagger完全适配knife4j.*
 * 生产模式下不需要文档及sheepyu.swagger.product=true即可
 *
 * @author ygq
 * @date 2023-01-14 10:54
 **/
@Configuration
@EnableSwagger2
@EnableConfigurationProperties({SwaggerProperties.class})
@AllArgsConstructor
public class SheepyuSwaggerAutoConfiguration {
    private final SwaggerProperties swaggerProperties;

    @Bean
    public DocketFactory docketFactory() {
        return new DocketFactory(swaggerProperties);
    }
}
