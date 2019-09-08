package com.baoge.config;

import com.baoge.bean.RainBow;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry               current bean definition registry
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean red = registry.containsBeanDefinition("com.baoge.bean.Red");
        boolean blue = registry.containsBeanDefinition("com.baoge.bean.Blue");
        if (red && blue) {
            // 指定bean的定义信息（类型，作用域）
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(RainBow.class);
            registry.registerBeanDefinition("rainBow", rootBeanDefinition);
        }
    }
}
