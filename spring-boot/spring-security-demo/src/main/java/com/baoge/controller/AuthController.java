package com.baoge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    /**
     * 登录成功后重定向地址
     */
    @RequestMapping("/loginSuccess")
    @ResponseBody
    public String loginSuccess() {
        return "登录成功";
    }
}
