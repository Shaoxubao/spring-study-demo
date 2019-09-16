package com.baoge.constructorinject;

import org.springframework.stereotype.Component;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/14
 */

@Component
public class InterfaceB {

    private InterfaceA interfaceA;

//    public InterfaceB() {
//        System.out.println("InterfaceB无参构造方法调用...");
//    }

    public InterfaceB(InterfaceA a) {
        this.interfaceA = a;
        System.out.println("InterfaceB有参构造方法调用...");
    }

}
