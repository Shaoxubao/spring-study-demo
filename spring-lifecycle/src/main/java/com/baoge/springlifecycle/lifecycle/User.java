package com.baoge.springlifecycle.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class User implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {

    public User() {
        System.out.println("User 构造方法执行了...");
    }

    private String name;

    @Value("张三")
    public void setName(String name) {
        System.out.println("setName 方法执行了...");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("setBeanName 方法执行了...");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("setBeanFactory 方法执行了...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext 方法执行了...");
    }

    @PostConstruct
    public void init() {
        System.out.println("init 方法执行了...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet 方法执行了...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy 方法执行了...");
    }

}
