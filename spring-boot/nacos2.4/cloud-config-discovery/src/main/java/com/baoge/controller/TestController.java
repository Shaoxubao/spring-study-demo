package com.baoge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

    @Value("${user.Name}")
    private String userName;

    @GetMapping("/hello")
    public String hello() {
        return "Hello " + userName;
    }

}