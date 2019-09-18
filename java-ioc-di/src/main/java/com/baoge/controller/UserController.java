package com.baoge.controller;

import com.baoge.DI.Inject;
import com.baoge.service.UserService;

/**
 * @Author shaoxubao
 * @Date 2019/9/18 17:56
 */
public class UserController {

    @Inject
    private UserService userService;

    public void save() {
        userService.save();
    }

    public UserController() {
    }

}
