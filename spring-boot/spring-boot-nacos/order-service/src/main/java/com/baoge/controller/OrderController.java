package com.baoge.controller;

import com.alibaba.fastjson.JSONObject;
import com.baoge.model.FeignResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/get2", method = RequestMethod.POST)
    public String get2(@RequestBody JSONObject json) {
        System.out.println("订单查询:" + json);
        return "订单:" + port;
    }

    @RequestMapping(value = "/get3", method = RequestMethod.POST)
    public String get3(@RequestHeader(value = "token", required = false) String token, @RequestBody JSONObject json) {
        System.out.println("订单查询:" + json);
        System.out.println("header:" + token);
        return "订单:" + port;
    }

    @RequestMapping(value = "/get4", method = RequestMethod.POST)
    public FeignResult get3(@RequestBody JSONObject json) {
        System.out.println("订单查询:" + json);
        FeignResult result = new FeignResult();
        result.setId("1");
        result.setStatus("200");
        return result;
    }


}