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

    public void setInterfaceC(InterfaceC interfaceC) {
        this.interfaceC = interfaceC;
    }

}

