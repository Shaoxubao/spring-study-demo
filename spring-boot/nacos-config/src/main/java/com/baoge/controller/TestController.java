package com.baoge.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@Slf4j
@RefreshScope
public class TestController {
    @Value("${blog.name}")
    private String blogName;

    @GetMapping("/get")
    public String get() {
        return "ConfigController#get blog name = " + blogName;
    }
}
