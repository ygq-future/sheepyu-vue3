package top.sheepyu.module.system.api;

import top.sheepyu.module.system.enums.SystemConfigKeyEnum;

/**
 * @author ygq
 * @date 2023-01-21 14:49
 **/
public interface ConfigApi {
    <T> T get(SystemConfigKeyEnum configKey);
}
