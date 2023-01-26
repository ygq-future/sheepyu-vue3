package top.sheepyu.framework.file.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.sheepyu.framework.file.config.FileProperties.BaseConfig;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-24 18:06
 **/
@Configuration
@EnableConfigurationProperties({FileProperties.class})
@ComponentScan({"top.sheepyu.framework.file.core.oss"})
public class SheepyuFileAutoConfiguration implements WebMvcConfigurer {
    @Resource
    private FileProperties fileProperties;

    @Bean
    public FileUploadFactory fileUploadFactory() {
        return new FileUploadFactory();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        BaseConfig localConfig = fileProperties.getConfig().get(FileUploadTypeEnum.LOCAL.getCode());
        if (localConfig != null) {
            registry.addResourceHandler("/file/**").addResourceLocations("file:" + localConfig.getPath() + "/");
        }
    }
}
