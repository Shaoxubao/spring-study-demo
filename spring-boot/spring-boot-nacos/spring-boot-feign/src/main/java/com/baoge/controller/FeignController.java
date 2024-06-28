package com.baoge.controller;

import com.alibaba.fastjson.JSONObject;
import com.baoge.client.MyFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FeignController {
    @Autowired
    private MyFeignClient feignClient;

    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public String getOrder(@RequestBody JSONObject req) {
        JSONObject json = new JSONObject();
        json.put("orderId", 123);
        String msg = feignClient.getOrderWithHeader(json);
        return "Hello " + msg;
    }
}
