package com.baoge.controller;

import com.baoge.entity.Order;
import com.baoge.entity.User;
import com.baoge.service.OrderService;
import com.baoge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multi")
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/getUser")
    public User getUser() {
        return userService.getUser();
    }

    @GetMapping("/getOrder")
    public Order getOrder() {
        return orderService.getOrder();
    }
}
