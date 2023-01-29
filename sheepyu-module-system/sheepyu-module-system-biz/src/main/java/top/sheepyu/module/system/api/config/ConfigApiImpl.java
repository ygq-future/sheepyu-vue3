package top.sheepyu.module.system.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.sheepyu.module.system.enums.config.SystemConfigKeyEnum;
import top.sheepyu.module.system.service.config.SystemConfigService;

import javax.annotation.Resource;

/**
 * @author ygq
 * @date 2023-01-21 14:50
 **/
@Service
@Slf4j
public class ConfigApiImpl implements ConfigApi {
    @Resource
    private SystemConfigService systemConfigService;

    @Override
    public <T> T get(SystemConfigKeyEnum configKey) {
        return systemConfigService.get(configKey);
    }
}
