package top.sheepyu.module.system.api.config;

import top.sheepyu.module.system.enums.config.SystemConfigKeyEnum;

/**
 * @author ygq
 * @date 2023-01-21 14:49
 **/
public interface ConfigApi {
    <T> T get(SystemConfigKeyEnum configKey);
}
