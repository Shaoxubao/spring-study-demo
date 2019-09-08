package com.baoge.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

@Component
public class Dog implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Dog() {
        System.out.println("dog constructor...");
    }

    // 对象创建并赋值之后调用
    @PostConstruct
    public void init() {
        System.out.println("dog @PostConstruct...");
    }

    // 容器移除对象之前
    @PreDestroy
    public void destroy() {
        System.out.println("dog @PreDestroy...");
    }


    /**
     * @see BeanInitializationException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
