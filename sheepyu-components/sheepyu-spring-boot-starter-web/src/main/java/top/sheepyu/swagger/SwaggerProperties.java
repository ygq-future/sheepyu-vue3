package top.sheepyu.swagger;

import com.github.xiaoymin.knife4j.core.extend.OpenApiExtendSetting;
import com.github.xiaoymin.knife4j.core.model.MarkdownProperty;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jHttpBasic;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "sheepyu.swagger")
public class SwaggerProperties {
    private boolean enable;
    private boolean product;
    private String title;
    private String description;
    private String version;
    private Contact contact;
    private String basePackage;
    private String host;
    private Knife4jHttpBasic basic;
    private OpenApiExtendSetting setting;
    private List<MarkdownProperty> documents;
    private List<GlobalOperationParameter> globalParameters;

    @Data
    public static class Contact {
        private String name;
        private String url;
        private String email;
    }

    @Data
    public static class GlobalOperationParameter {
        private String name;
        private String description = "全局参数";
        private Boolean required = false;
        private String defaultValue = "";
        private Boolean allowEmptyValue = true;
    }
}
