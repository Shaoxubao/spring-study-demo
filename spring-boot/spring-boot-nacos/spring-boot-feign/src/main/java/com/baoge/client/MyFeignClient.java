package com.baoge.client;

import com.alibaba.fastjson.JSONObject;
import com.baoge.model.FeignResult;
import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("order-service") // 服务提供者的spring.application.name
public interface MyFeignClient {

    @GetMapping("/order/get")
    String getOrder1();
    @PostMapping("/order/get2")
    String getOrder(@RequestBody JSONObject json);

    @PostMapping("/order/get3")
    String getOrder3(@RequestBody JSONObject json);

    @PostMapping("/order/get3")
    String getOrderWithHeader(@RequestBody JSONObject json);

    @PostMapping("/order/get4")
    FeignResult getOrderWithMyReturn(@RequestBody JSONObject req);

    @PostMapping("/order/get5")
    FeignResult getOrderWithMyReturn2(JSONObject req);
}