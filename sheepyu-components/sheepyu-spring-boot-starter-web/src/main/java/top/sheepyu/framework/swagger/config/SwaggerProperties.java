package top.sheepyu.framework.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "sheepyu.swagger")
public class SwaggerProperties {
    private ApiGroup baseInfo = new ApiGroup();
    private Map<String, ApiGroup> apiGroups = new HashMap<>();
    private List<GlobalOperationParameter> globalParameters = new ArrayList<>();

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    public static class GlobalOperationParameter {
        private String name;
        private String description = "";
        private Boolean required = false;
        private String defaultValue = "";
        private Boolean allowEmptyValue = true;
    }

    @Data
    public static class ApiGroup {
        private String version;
        private String basePackage;
        private String host;
        private String title;
        private String serviceUrl;
        private String description;
        private Contact contact = new Contact();
    }
}
