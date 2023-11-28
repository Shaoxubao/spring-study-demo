package com.baoge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logback")
public class TestController {

    private final static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Value("${msg}")
    private String msg;

    @RequestMapping("/showInfo")
    public String showInfo() {
        logger.info("记录日志");
        return "Hello Logback " + msg;
    }
}
