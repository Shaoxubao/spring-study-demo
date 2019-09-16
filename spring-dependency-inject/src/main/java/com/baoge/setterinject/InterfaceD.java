package com.baoge.setterinject;

import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/14
 */

@Component
public class InterfaceD {

    private InterfaceC interfaceC;

//    public InterfaceD() {
//        System.out.println("InterfaceD无参构造方法调用...");
//    }

    public void setInterfaceC(InterfaceC interfaceC) {
        this.interfaceC = interfaceC;
        System.out.println("InterfaceD setter 方法调用...");
    }

    public String hello() {
        return "hello world";
    }

}

