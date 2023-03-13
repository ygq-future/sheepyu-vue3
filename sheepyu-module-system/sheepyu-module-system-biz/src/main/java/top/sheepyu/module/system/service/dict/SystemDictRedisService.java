package top.sheepyu.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.module.system.dao.dict.SystemDictData;

import javax.annotation.Resource;
import java.util.HashSet;

import static top.sheepyu.module.system.constants.RedisConstants.SYSTEM_DICT_KEY;

/**
 * @author ygq
 * @date 2023-01-24 15:54
 **/
@Service
public class SystemDictRedisService {
    @Resource
    private RedisUtil redisUtil;

    public void loadDictType(String type, HashSet<SystemDictData> set) {
        if (CollUtil.isEmpty(set)) {
            deleteDictType(type);
            return;
        }
        //删除无关信息
        set.forEach(item -> {
            item.setCreator(null);
            item.setUpdater(null);
            item.setCreateTime(null);
            item.setUpdateTime(null);
            item.setDeleted(null);
            item.setRemark(null);
        });
        redisUtil.set(buildKey(type), JSONUtil.toJsonStr(set, new JSONConfig()));
    }

    public void deleteDictType(String type) {
        redisUtil.del(buildKey(type));
    }

    public void addDictData(String type, SystemDictData dictData) {
        HashSet<SystemDictData> set = listByType(type);
        if (CollUtil.isEmpty(set)) {
            HashSet<SystemDictData> hashSet = new HashSet<>();
            hashSet.add(dictData);
            loadDictType(type, hashSet);
            return;
        }

        set.remove(dictData);
        set.add(dictData);
        loadDictType(type, set);
    }

    public void delDictData(String type, SystemDictData dictData) {
        HashSet<SystemDictData> set = listByType(type);
        if (CollUtil.isEmpty(set)) {
            return;
        }

        set.remove(dictData);
        loadDictType(type, set);
    }

    public void clear() {
        redisUtil.delPrefix(SYSTEM_DICT_KEY);
    }

    public HashSet<SystemDictData> listByType(String type) {
        HashSet<SystemDictData> result = redisUtil.getJSONObj(buildKey(type), new TypeReference<HashSet<SystemDictData>>() {
        });
        return result == null ? new HashSet<>() : result;
    }

    private String buildKey(String type) {
        return SYSTEM_DICT_KEY.concat(type);
    }
}
