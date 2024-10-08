package com.baoge.controller;

import com.alibaba.fastjson.JSONObject;
import com.baoge.client.MyFeignClient;
import com.baoge.client.MyFeignClient2;
import com.baoge.model.FeignResult;
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

    @Autowired
    private MyFeignClient2 myFeignClient2;

    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public String getOrder(@RequestBody JSONObject req) {
        JSONObject json = new JSONObject();
        json.put("orderId", 123);
        String msg = feignClient.getOrderWithHeader(json);
        return "Hello " + msg;
    }

    @RequestMapping(value = "/getOrder4", method = RequestMethod.POST)
    public String getOrder4(@RequestBody JSONObject req) {
        JSONObject json = new JSONObject();
        json.put("orderId", 123);
        FeignResult result = feignClient.getOrderWithMyReturn(json);
        return "Hello " + result;
    }

    @RequestMapping(value = "/getOrder5", method = RequestMethod.POST)
    public String getOrder5(@RequestBody JSONObject req) {
        JSONObject json = new JSONObject();
        json.put("orderId", 123);
        FeignResult result = feignClient.getOrderWithMyReturn2(json);
        return "Hello " + result;
    }

    /**
     * 启动order-service
     * 调用此mock方法，需先启动com.baoge.MockServer
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        String mockHello = myFeignClient2.getMockHello();
        System.out.println(mockHello);
        return mockHello;
    }
}
