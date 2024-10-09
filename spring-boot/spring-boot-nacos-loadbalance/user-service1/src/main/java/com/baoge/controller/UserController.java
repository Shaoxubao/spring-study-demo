package com.baoge.controller;

import com.baoge.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String get() {
        System.out.println("用户查询订单.........................");
        return "订单返回:" + orderService.getOrder();
    }
}
