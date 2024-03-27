package com.baoge.lua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LuaScriptService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String executeLuaScript() {
        // Lua脚本内容
        String luaScript = "return 'Hello, Lua!'";

        // 创建RedisScript对象
        RedisScript<String> script = new DefaultRedisScript<>(luaScript, String.class);

        // 执行Lua脚本
        String result = redisTemplate.execute(script, Collections.emptyList());

        return result;
    }
}
