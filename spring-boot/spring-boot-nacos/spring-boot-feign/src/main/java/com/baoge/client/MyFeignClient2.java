package com.baoge.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "http://127.0.0.1:10080", name = "hello")
public interface MyFeignClient2 {

    @GetMapping("hello")
    String getMockHello();
}