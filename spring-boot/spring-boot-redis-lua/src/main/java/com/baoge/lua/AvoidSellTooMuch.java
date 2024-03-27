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
public class AvoidSellTooMuch extends Thread {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public AvoidSellTooMuch(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {
        // 在本线程内，模拟在单位时间内发5个请求
        boolean sellFlag = canSell("Computer");
        if (sellFlag) {
            System.out.println(Thread.currentThread().getName() + " can buy.");
        } else {
            System.out.println(Thread.currentThread().getName() + " can not buy.");
        }
    }

    // 判断当前请求是否会导致超卖
    public boolean canSell(String modelName) {
        // 防止超卖的脚本
        String script = "local existedNum=tonumber(redis.call('get',KEYS[1]))\n" +
                "if(existedNum>0) then\n" +
                "   redis.call('incrby',KEYS[1],-1)\n" +
                "   return existedNum\n" +
                "end\n" +
                "return -1\n";

        // 创建RedisScript对象
        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
        // 执行Lua脚本
        String result = String.valueOf(redisTemplate.execute(redisScript, Arrays.asList(modelName), modelName, modelName));

        return !"-1".equals(result);
    }
}
