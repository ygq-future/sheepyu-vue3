package top.sheepyu.framework.redis.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ygq
 * @date 2023-01-15 14:38
 **/
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public boolean setNx(String key, long timeout) {
        return setNx(key, timeout, TimeUnit.SECONDS);
    }

    public boolean setNx(String key, long timeout, TimeUnit timeUnit) {
        return setNx(key, "1", timeout, timeUnit);
    }

    public boolean setNx(String key, String value, long timeout, TimeUnit timeUnit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        return BooleanUtil.isTrue(result);
    }

    public <T> T getJSONObj(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSONUtil.toBean(value.toString(), clazz);
    }

    public <T> T getJSONObj(String key, TypeReference<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSONUtil.toBean(value.toString(), type, false);
    }

    public <T> T getObj(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public void sadd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void put(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void putAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void hDel(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public long ttl(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire == null ? 0 : expire;
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void inr(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void delPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix.concat("*"));
        redisTemplate.delete(keys);
    }
}
