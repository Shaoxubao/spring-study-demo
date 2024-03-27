package com.baoge.controller;

import com.baoge.lua.AvoidSellTooMuch;
import com.baoge.lua.LuaLimitByCount;
import com.baoge.utils.StaticMethodGetBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/limit", method = RequestMethod.GET)
    public void limit() {
        // 通过for循环启动了3个线程，并通过它们的run方法在短时间里调用5次LimitByCount类的canVisit方法
        for (int cnt = 0; cnt < 3; cnt++) {
            LuaLimitByCount luaLimitByCount = StaticMethodGetBean.getBean(LuaLimitByCount.class);
            assert luaLimitByCount != null;
            luaLimitByCount.start();
        }
    }

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public void sell() {
        // 预设5个电脑商品，并设置10秒的生存时间
        redisTemplate.opsForValue().set("Computer", "5", 10, TimeUnit.SECONDS);
        // 开启10个线程来抢购
        for (int cnt = 0; cnt < 10; cnt++) {
            AvoidSellTooMuch avoidSellTooMuch = StaticMethodGetBean.getBean(AvoidSellTooMuch.class);
            avoidSellTooMuch.start();
        }
    }
}
