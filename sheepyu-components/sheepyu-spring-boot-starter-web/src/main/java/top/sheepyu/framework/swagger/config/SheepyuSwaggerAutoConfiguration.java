package top.sheepyu.framework.swagger.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.sheepyu.framework.swagger.config.SwaggerProperties.GlobalOperationParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
@Slf4j
public class SheepyuSwaggerAutoConfiguration {
    private SwaggerProperties swaggerProperties;
    private Environment environment;

    @Bean
    @ConditionalOnProperty(prefix = "sheepyu.swagger", value = "product")
    public ProductionSecurityFilter productionSecurityFilter() {
        boolean prod = false;
        ProductionSecurityFilter p;
        if (swaggerProperties == null) {
            if (this.environment != null) {
                String prodStr = this.environment.getProperty("knife4j.production");
                if (log.isDebugEnabled()) {
                    log.debug("swagger.production:{}", prodStr);
                }
                prod = BooleanUtil.toBoolean(prodStr);
            }
            p = new ProductionSecurityFilter(prod);
        } else {
            p = new ProductionSecurityFilter(swaggerProperties.isProduct());
        }
        return p;
    }

    @Bean
    @ConditionalOnMissingBean({OpenApiExtensionResolver.class})
    @ConditionalOnProperty(prefix = "sheepyu.swagger", value = "enable")
    public OpenApiExtensionResolver markdownResolver() {
        OpenApiExtendSetting setting = swaggerProperties.getSetting();
        if (setting == null) {
            setting = new OpenApiExtendSetting();
        }
        if (StrUtil.isNotBlank(swaggerProperties.getHost())) {
            setting.setEnableHost(true);
            setting.setEnableHostText(swaggerProperties.getHost());
        }
        return new OpenApiExtensionResolver(setting, swaggerProperties.getDocuments());
    }

    @Bean
    @ConditionalOnMissingBean({SecurityBasicAuthFilter.class})
    @ConditionalOnProperty(name = {"sheepyu.swagger.basic.enable"}, havingValue = "true")
    public SecurityBasicAuthFilter securityBasicAuthFilter() {
        boolean enableSwaggerBasicAuth = false;
        String dftUserName = "admin";
        String dftPass = "123321";
        SecurityBasicAuthFilter securityBasicAuthFilter = null;
        if (swaggerProperties == null) {
            if (this.environment != null) {
                String enableAuth = this.environment.getProperty("knife4j.basic.enable");
                enableSwaggerBasicAuth = BooleanUtil.toBoolean(enableAuth);
                if (enableSwaggerBasicAuth) {
                    String pUser = this.environment.getProperty("knife4j.basic.username");
                    String pPass = this.environment.getProperty("knife4j.basic.password");
                    if (pUser != null && !"".equals(pUser)) {
                        dftUserName = pUser;
                    }

                    if (pPass != null && !"".equals(pPass)) {
                        dftPass = pPass;
                    }
                }
                securityBasicAuthFilter = new SecurityBasicAuthFilter(enableSwaggerBasicAuth, dftUserName, dftPass);
            }
        } else if (swaggerProperties.getBasic() == null) {
            securityBasicAuthFilter = new SecurityBasicAuthFilter(enableSwaggerBasicAuth, dftUserName, dftPass);
        } else {
            securityBasicAuthFilter = new SecurityBasicAuthFilter(swaggerProperties.getBasic().isEnable(), swaggerProperties.getBasic().getUsername(), swaggerProperties.getBasic().getPassword());
        }

        return securityBasicAuthFilter;
    }

    @Bean
    @ConditionalOnProperty(prefix = "sheepyu.swagger", value = "enable")
    public Docket createDocket() {
        //构造全局参数
        List<GlobalOperationParameter> parameters = swaggerProperties.getGlobalParameters();
        List<RequestParameter> globalParameters = new ArrayList<>();

        if (CollUtil.isNotEmpty(parameters)) {
            for (GlobalOperationParameter parameter : parameters) {
                globalParameters.add(new RequestParameterBuilder()
                        .name(parameter.getName())
                        .description(parameter.getDescription())
                        .in(ParameterType.HEADER)
                        .required(parameter.getRequired())
                        .query(builder -> builder.defaultValue(parameter.getDefaultValue()).allowEmptyValue(parameter.getAllowEmptyValue()))
                        .build());
            }
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .host(swaggerProperties.getHost())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .build()
                .globalRequestParameters(globalParameters)
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private static List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(HttpHeaders.AUTHORIZATION, "Authorization", "header"));
    }

    private static List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(securityReferences())
                // 通过 PathSelectors.regex("^(?!auth).*$")，排除包含 "auth" 的接口不需要使用securitySchemes
                .operationSelector(o -> o.requestMappingPattern().matches("^(?!auth).*$"))
                .build());
    }

    private static List<SecurityReference> securityReferences() {
        return Collections.singletonList(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes()));
    }

    private static AuthorizationScope[] authorizationScopes() {
        return new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")};
    }

    private ApiInfo apiInfo() {
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }
}
