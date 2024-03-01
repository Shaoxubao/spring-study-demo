package com.baoge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/get")
    public String get() {
        System.out.println("订单查询");
        return "订单:" + port;
    }


}