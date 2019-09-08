package com.baoge.bean;

import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

@Component
public class Car {

    public Car() {
        System.out.println("car constructor...");
    }

    public void init() {
        System.out.println("car init...");
    }

    public void destroy() {
        System.out.println("car destroy...");
    }

}
