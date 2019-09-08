package com.baoge.bean;

import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

// 默认加在ioc容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作
@Component
public class Boss {

    private Car car;

    // 构造器要用的组件，都是从容器中获取
//    @Autowired                             // 构造器位置
    public Boss(/** @Autowired */ Car car) { // 参数位置
        this.car = car;
        System.out.println("Boss 有参构造器...");
    }

    public Car getCar() {
        return car;
    }

//     @Autowired
    // 标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值；
    // 方法使用的参数，自定义类型的值从ioc容器中获取
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }
}
