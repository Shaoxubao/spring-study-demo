package com.baoge.lua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope("prototype")
public class LuaLimitByCount extends Thread {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public LuaLimitByCount(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void run() {
        // 在本线程内，模拟在单位时间内发5个请求
        for (int visitNum = 0; visitNum < 5; visitNum++) {
            boolean visitFlag = canVisit(Thread.currentThread().getName(), "3", "10");
            if (visitFlag) {
                System.out.println(Thread.currentThread().getName() + " can visit.");
            } else {
                System.out.println(Thread.currentThread().getName() + " can not visit.");
            }
        }
    }

    // 判断是否需要限流
    public boolean canVisit(String modelName, String limitNum, String limitTime) {
        String script = "local obj=KEYS[1] \n" +
                "local limitNum=tonumber(ARGV[1]) \n" +
                "local curVisitNum=tonumber(redis.call('get',obj) or \"0\")\n" +
                "if curVisitNum+1>limitNum then \n" +
                "   return 0\n" +
                "else  \n" +
                "   redis.call(\"incrby\",obj,\"1\")\n" +
                "   redis.call(\"expire\",obj,tonumber(ARGV[2]))\n" +
                "   return curVisitNum+1\n" +
                "end";

        // 创建RedisScript对象
        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
        // 执行Lua脚本
        String result = String.valueOf(redisTemplate.execute(redisScript, Arrays.asList(modelName), limitNum, limitTime));

        return !"0".equals(result);   // 不能继续访问
    }
}