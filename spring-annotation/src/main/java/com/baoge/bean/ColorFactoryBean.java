package com.baoge.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

// 创建一个Spring定义的FactoryBean
public class ColorFactoryBean implements FactoryBean<Color> {
    public Color getObject() throws Exception {

        System.out.println("ColorFactoryBean...getObject()...");
        return new Color();
    }

    public Class<?> getObjectType() {
        return Color.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
