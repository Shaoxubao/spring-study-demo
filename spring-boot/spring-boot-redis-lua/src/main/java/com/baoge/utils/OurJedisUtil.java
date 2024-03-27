package com.baoge.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class OurJedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 设置带过期时间的缓存
    public Boolean set(String key, Object value, long time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    // 设置缓存
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 根据 key 获得缓存
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 根据 key 删除缓存
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    // 根据 keys 集合批量删除缓存
    public Long delete(Set<String> keys) {
        return redisTemplate.delete(keys);
    }

    // 根据正则表达式匹配 keys 获取缓存
    public Set<String> getKeysByPattern(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void hmput(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Boolean expire(String key, long timeOut) {
        return redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }
}
