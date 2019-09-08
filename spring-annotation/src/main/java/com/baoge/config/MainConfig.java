package com.baoge.config;

import com.baoge.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/7
 */

// @ComponentScan 指定要扫描的包
// excludeFilters 排除扫描
// includeFilters 指定扫描

@Configuration
//@ComponentScan(value = "com.baoge", excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class, Service.class})
//})
@ComponentScan(value = "com.baoge", includeFilters = {
//        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class}),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BookServiceImpl.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class}) // 自定义MyTypeFilter
}, useDefaultFilters = false)
public class MainConfig {

    // 给容器中注册一个bean,id默认为方法名
    // @Bean("")指定id名
    @Bean("person01")
    public Person person() {
        return new Person(22, "wangwu");
    }

}
