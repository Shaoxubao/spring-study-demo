package com.baoge.lua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LuaScriptFileService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String executeLuaScriptFromFile() {
        // 创建RedisScript对象并指定Lua脚本文件的路径
        RedisScript<String> script = new DefaultRedisScript<>("path/myscript.lua", String.class);

        // 执行Lua脚本
        String result = redisTemplate.execute(script, Collections.emptyList());

        return result;
    }
}
