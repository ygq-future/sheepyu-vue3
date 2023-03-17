package top.sheepyu.framework.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author ygq
 * @date 2022-12-03 16:23
 **/
@Data
@ConfigurationProperties(prefix = "sheepyu.web")
public class WebProperties {
    private Api app = new Api("/app-api", "**.controller.app.**");
    private Api admin = new Api("/admin-api", "**.controller.admin.**");
    private boolean demo;
    private List<String> demoExcludeUrl = Collections.emptyList();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Api {
        private String prefix;
        private String controller;
    }
}
