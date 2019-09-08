package com.baoge.config;

import com.baoge.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

//使用@PropertySource读取外部配置文件中的k/v保存到运行的环境变量中;加载完外部的配置文件以后使用${}取出配置文件的值

@PropertySource(value = {"classpath:/person.properties"})
@Configuration
public class MainConfigPropertyValue {

    @Bean
    public Person person() {
        return new Person();
    }

}
