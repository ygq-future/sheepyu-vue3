package top.sheepyu.framework.swagger.factory;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import top.sheepyu.framework.swagger.config.SwaggerProperties;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ygq
 * @date 2024-07-11 17:39
 **/
@AllArgsConstructor
public class DocketFactory {
    private final SwaggerProperties swaggerProperties;

    public Docket buildDocket(String groupName) {
        SwaggerProperties.ApiGroup baseInfo = swaggerProperties.getBaseInfo();
        SwaggerProperties.ApiGroup apiGroup = swaggerProperties.getApiGroups().get(groupName);
        if (apiGroup == null) {
            throw new RuntimeException("groupName: " + groupName + " config not found!");
        }

        String host = Optional.ofNullable(apiGroup.getHost()).orElse(baseInfo.getHost());
        String basePackage = Optional.ofNullable(apiGroup.getBasePackage()).orElse(baseInfo.getBasePackage());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(buildApiInfo(apiGroup))
                .host(host)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build()
                .globalRequestParameters(buildGlobalParameter())
                .securitySchemes(securitySchemes());
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(HttpHeaders.AUTHORIZATION, "Authorization", "header"));
    }

    private ApiInfo buildApiInfo(SwaggerProperties.ApiGroup apiGroup) {
        SwaggerProperties.ApiGroup baseInfo = swaggerProperties.getBaseInfo();
        SwaggerProperties.Contact contact = apiGroup.getContact();
        SwaggerProperties.Contact baseContact = baseInfo.getContact();

        String title = Optional.ofNullable(apiGroup.getTitle()).orElse(baseInfo.getTitle());
        String description = Optional.ofNullable(apiGroup.getDescription()).orElse(baseInfo.getDescription());
        String version = Optional.ofNullable(apiGroup.getVersion()).orElse(baseInfo.getVersion());
        String host = Optional.ofNullable(apiGroup.getHost()).orElse(baseInfo.getHost());
        String name = Optional.ofNullable(contact.getName()).orElse(baseContact.getName());
        String url = Optional.ofNullable(contact.getUrl()).orElse(baseContact.getUrl());
        String email = Optional.ofNullable(contact.getEmail()).orElse(baseContact.getEmail());
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact(name, url, email))
                .version(version)
                .termsOfServiceUrl(host)
                .build();
    }

    private List<RequestParameter> buildGlobalParameter() {
        return swaggerProperties.getGlobalParameters()
                .stream().map(parameter -> new RequestParameterBuilder()
                        .name(parameter.getName())
                        .description(parameter.getDescription())
                        .in(ParameterType.HEADER)
                        .required(parameter.getRequired())
                        .query(builder -> builder.defaultValue(parameter.getDefaultValue()).allowEmptyValue(parameter.getAllowEmptyValue()))
                        .build()).collect(Collectors.toList());
    }
}
