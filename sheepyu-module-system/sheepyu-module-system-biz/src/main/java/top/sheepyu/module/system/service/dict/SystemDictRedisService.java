package top.sheepyu.module.system.service.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
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

    public void loadDictType(Long typeId, HashSet<SystemDictData> set) {
        if (CollUtil.isEmpty(set)) {
            deleteDictType(typeId);
            return;
        }
        redisUtil.set(buildKey(typeId), JSONUtil.toJsonStr(set));
    }

    public void deleteDictType(Long id) {
        redisUtil.del(buildKey(id));
    }

    public void addDictData(Long typeId, SystemDictData dictData) {
        HashSet<SystemDictData> set = listByType(typeId);
        if (CollUtil.isEmpty(set)) {
            HashSet<SystemDictData> hashSet = new HashSet<>();
            hashSet.add(dictData);
            loadDictType(typeId, hashSet);
            return;
        }

        set.add(dictData);
        loadDictType(typeId, set);
    }

    public void delDictData(Long typeId, SystemDictData dictData) {
        HashSet<SystemDictData> set = listByType(typeId);
        if (CollUtil.isEmpty(set)) {
            return;
        }

        set.remove(dictData);
        loadDictType(typeId, set);
    }

    public void clear() {
        redisUtil.delPrefix(SYSTEM_DICT_KEY);
    }

    public HashSet<SystemDictData> listByType(Long typeId) {
        return redisUtil.getJSONObj(buildKey(typeId), new TypeReference<HashSet<SystemDictData>>() {
        });
    }

    private String buildKey(Long id) {
        return SYSTEM_DICT_KEY.concat(id.toString());
    }
}
