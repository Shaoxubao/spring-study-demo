package com.baoge.bean;

import com.baoge.annotation.MyComponent;
import com.baoge.annotation.MyValue;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/18
 */

@MyComponent
//@MyComponent(scope="prototype")
public class User {

    @MyValue("1")
    private Integer id;
    @MyValue("zhangsan")
    private String name;
    @MyValue("zhangsan")
    private String password;

    public User() {
        System.out.println("无参构造方法执行");
    }

    public void login(){
        System.out.println("用户登录：id=" + id + ", name=" + name + ", password=" + password);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
