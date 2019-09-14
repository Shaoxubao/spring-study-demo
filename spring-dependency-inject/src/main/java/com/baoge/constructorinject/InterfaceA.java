package com.baoge.constructorinject;

import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/14
 */

@Component
public class InterfaceA {

    private InterfaceB interfaceB;

//    public InterfaceA() {
//        System.out.println("InterfaceA无参构造方法调用...");
//    }

    public InterfaceA(InterfaceB b) {
        this.interfaceB = b;
        System.out.println("InterfaceA有参构造方法调用...");
    }

}
