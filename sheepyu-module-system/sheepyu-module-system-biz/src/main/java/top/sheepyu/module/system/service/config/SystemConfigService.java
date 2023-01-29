package top.sheepyu.module.system.service.config;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigCreateVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigUpdateVo;
import top.sheepyu.module.system.dao.config.SystemConfig;
import top.sheepyu.module.system.enums.config.SystemConfigKeyEnum;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-21 14:27
 **/
public interface SystemConfigService extends IServiceX<SystemConfig> {
    /**
     * 获取指定的配置
     *
     * @param configKey 系统配置key的枚举类
     * @param <T>       泛型
     * @return 返回配置值
     */
    <T> T get(SystemConfigKeyEnum configKey);

    void createConfig(@Valid SystemConfigCreateVo createVo);

    void updateConfig(@Valid SystemConfigUpdateVo updateVo);

    void deleteConfig(Long id);

    List<SystemConfig> listConfig(String keyword);

    SystemConfig findById(Long id);
}
