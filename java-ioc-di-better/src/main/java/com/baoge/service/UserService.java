package com.baoge.service;

import com.baoge.annotation.MyAutowired;
import com.baoge.annotation.MyComponent;
import com.baoge.bean.User;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/18
 */

@MyComponent
public class UserService {

    @MyAutowired
    User user1;

    @MyAutowired
    User user2;

    public void userLogin(){
        System.out.println("用户1:" + user1);
        user1.login();
        System.out.println("用户2:" + user2);
        user2.login();
    }

}
