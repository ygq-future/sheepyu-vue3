package top.sheepyu.framework.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;

/**
 * @author ygq
 * @date 2023-01-25 16:44
 **/
@ConfigurationProperties(prefix = "sheepyu.file")
@Data
public class FileProperties {
    private Map<String, BaseConfig> config = Collections.emptyMap();
}
