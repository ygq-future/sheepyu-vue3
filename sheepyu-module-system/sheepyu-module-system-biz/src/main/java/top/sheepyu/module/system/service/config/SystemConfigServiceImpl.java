package top.sheepyu.module.system.service.config;

import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigCreateVo;
import top.sheepyu.module.system.controller.admin.config.vo.SystemConfigUpdateVo;
import top.sheepyu.module.system.dao.config.SystemConfig;
import top.sheepyu.module.system.dao.config.SystemConfigMapper;
import top.sheepyu.module.system.enums.config.SystemConfigKeyEnum;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.constants.RedisConstants.SYSTEM_CONFIG_KEY;
import static top.sheepyu.module.system.convert.config.SystemConfigConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-21 14:27
 **/
@Service
@Slf4j
@Validated
public class SystemConfigServiceImpl extends ServiceImplX<SystemConfigMapper, SystemConfig> implements SystemConfigService {
    @Resource
    private RedisUtil redisUtil;

    @PostConstruct
    public void loadData() {
        log.info("[config]配置key加载...");
        List<SystemConfig> list = list();
        Map<String, Object> map = new HashMap<>();
        list.forEach(item -> map.put(item.getConfigKey(), item.getConfigValue()));
        redisUtil.putAll(SYSTEM_CONFIG_KEY, map);
        log.info("[config]配置key加载完成");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(SystemConfigKeyEnum configKey) {
        Object obj = redisUtil.hGet(SYSTEM_CONFIG_KEY, configKey.getKey());
        if (obj == null) {
            throw exception(CONFIG_NOT_EXISTS);
        }

        Class<?> clazz = configKey.getClazz();
        //为什么要经过这么一个方法才行, 因为这个无论在数据库里还是在redis中
        //本来就是字符串, 如果直接进行强转肯定报错Cannot cast xxx
        //所以还要中间转一下
        return (T) strCastObject(obj.toString(), clazz);
    }

    @Override
    public void createConfig(SystemConfigCreateVo createVo) {
        SystemConfig config = CONVERT.convert(createVo);
        checkRepeatByFieldThrow(config, CONFIG_KEY_EXISTS, SystemConfig::getConfigKey);
        boolean result = save(config);
        if (result) {
            redisUtil.put(SYSTEM_CONFIG_KEY, config.getConfigKey(), config.getConfigValue());
        }
    }

    @Override
    public void updateConfig(SystemConfigUpdateVo updateVo) {
        //先把数据库中查出来的key保存起来, 因为修改vo中是没有key的
        String key = findByIdValidateExists(updateVo.getId()).getConfigKey();
        SystemConfig config = CONVERT.convert(updateVo);
        boolean result = updateById(config);
        if (result) {
            redisUtil.put(SYSTEM_CONFIG_KEY, key, config.getConfigValue());
        }
    }

    @Override
    public void deleteConfig(Long id) {
        SystemConfig config = findByIdValidateExists(id);
        boolean result = removeById(config);
        if (result) {
            redisUtil.hDel(SYSTEM_CONFIG_KEY, config.getConfigKey());
        }
    }

    @Override
    public List<SystemConfig> listConfig(String keyword) {
        return baseMapper.selectList(buildQuery()
                .likeIfPresent(SystemConfig::getName, keyword).or()
                .likeIfPresent(SystemConfig::getConfigKey, keyword).or()
                .likeIfPresent(SystemConfig::getRemark, keyword));
    }

    @Override
    public SystemConfig findById(Long id) {
        return findByIdValidateExists(id);
    }

    private SystemConfig findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, CONFIG_NOT_EXISTS);
    }

    private Object strCastObject(String value, Class<?> clazz) {
        if (clazz == Integer.class) {
            return Integer.parseInt(value);
        }
        if (clazz == Boolean.class) {
            return BooleanUtil.toBoolean(value);
        }
        if (clazz == String.class) {
            return value;
        }
        throw exception(NOT_SUPPORT_TYPE);
    }
}
