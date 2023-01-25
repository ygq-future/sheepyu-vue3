package top.sheepyu.framework.file.config;

import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-25 16:46
 **/
@Data
public class BaseConfig {
    private String keyId;
    private String keySecret;
    private String endpoint;
    private String bucket;
    private String path;
}
