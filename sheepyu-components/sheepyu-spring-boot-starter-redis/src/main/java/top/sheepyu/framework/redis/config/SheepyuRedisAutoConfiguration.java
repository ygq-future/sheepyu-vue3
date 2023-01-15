package top.sheepyu.framework.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.sheepyu.framework.redis.util.RedisUtil;

/**
 * @author ygq
 * @date 2023-01-15 14:32
 **/
@Configuration
public class SheepyuRedisAutoConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }
}
