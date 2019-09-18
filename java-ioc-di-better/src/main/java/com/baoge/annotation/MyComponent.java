package com.baoge.annotation;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/18
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**@Target 属性用于注明此注解用在什么位置,
 * ElementType.TYPE表示可用在类、接口、枚举上等*/
@Target(ElementType.TYPE)
/**@Retention 属性表示所定义的注解何时有效,
 * RetentionPolicy.RUNTIME表示在运行时有效*/
@Retention(RetentionPolicy.RUNTIME)
/**@interface 表示注解类型*/
public @interface MyComponent {

    /**为此注解定义scope属性*/
    public String scope() default "";

}
