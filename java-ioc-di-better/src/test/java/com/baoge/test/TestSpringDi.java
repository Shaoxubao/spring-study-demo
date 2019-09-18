package com.baoge.test;

import com.baoge.annotation.MyComponent;
import com.baoge.factory.AnnotationConfigApplicationContext;
import com.baoge.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/18
 */

@MyComponent
public class TestSpringDi {

    /**创建AnnotationConfigApplicationContext对象*/
    AnnotationConfigApplicationContext ctx;
    /**创建UserService对象*/
    UserService userService;
    /**
     * 初始化方法
     */
    @Before
    public void init(){
        // 实例化工厂类，传入bean/service/test三个包路径进行扫描
        ctx = new AnnotationConfigApplicationContext("com.baoge.bean", "com.baoge.service", "com.baoge.test");
        // 调用工厂的getBean方法动态获取对象
        userService = ctx.getBean("userService",UserService.class);
    }
    /**
     * 测试方法
     */
    @Test
    public void testSpringDi(){
        userService.userLogin();
    }
    /**
     * 销毁方法
     */
    @After
    public void close(){
        ctx.close();
    }
}
