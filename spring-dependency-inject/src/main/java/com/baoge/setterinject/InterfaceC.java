package com.baoge.setterinject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/14
 */

@Component
public class InterfaceC {

    private InterfaceD interfaceD;

//    public InterfaceC() {
//        System.out.println("InterfaceC无参构造方法调用...");
//    }

    @Autowired
    public void setInterfaceD(InterfaceD interfaceD) {
        this.interfaceD = interfaceD;
        System.out.println("InterfaceC setter 方法调用...");
    }

    public String callInterfaceD() {
        return interfaceD.hello();
    }

    public String hello() {
        return "hello InterfaceD";
    }

}
