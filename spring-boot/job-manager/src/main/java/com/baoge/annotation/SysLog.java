package com.baoge.annotation;

import java.lang.annotation.*;

/**
 * 定义系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {


    /**
     * 日志）
     *
     * @return
     */
    String name() default "";


    String beanClass() default "";
}
