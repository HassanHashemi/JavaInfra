package org.infra.decorators.caching.redis;

import jakarta.annotation.Resource;
import org.infra.decorators.caching.DistributedCache;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisDistributedCache implements DistributedCache {

    private final ValueOperations<String, String> keyValue;

    public RedisDistributedCache(RedisTemplate<String, String> keyValue) {
        keyValue.setDefaultSerializer(new StringRedisSerializer());
        this.keyValue = keyValue.opsForValue();
    }

    @Override
    public void set(String key, String value, Duration expirationTimeout) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Invalid key");

        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("Invalid value");

        this.keyValue.set(key, value, expirationTimeout);
    }

    @Override
    public String get(String key) {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Invalid key");

        return this.keyValue.get(key);
    }
}
