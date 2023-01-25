package top.sheepyu.framework.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-18 14:45
 **/
@ConfigurationProperties(prefix = "sheepyu.log")
@Data
@Validated
public class ApiLogProperties {
    //排除不需要记录日志的url, 需要加上请求前缀也就是admin-api或app-api
    private List<String> excludes = Collections.emptyList();
}
