package com.baoge.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

@Component
public class Red implements ApplicationContextAware, BeanNameAware,
        EmbeddedValueResolverAware {

    private ApplicationContext applicationContext;

    /**
     */
    public void setBeanName(String name) {
        System.out.println("当前bean的name：" + name);
    }

    /**
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("传入的ioc:" + applicationContext);
    }

    /**
     * Set the StringValueResolver to use for resolving embedded definition values.
     *
     * @param resolver
     */
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String resolveStringValue = resolver.resolveStringValue("你好！${os.name} 我是#{20 * 18}");
        System.out.println("解析的字符串：" + resolveStringValue);
    }
}
