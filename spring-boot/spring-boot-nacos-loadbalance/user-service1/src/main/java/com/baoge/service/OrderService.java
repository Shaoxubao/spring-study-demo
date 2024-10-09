package com.baoge.service;

import com.baoge.config.CustomRibbonConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "${order.service.client}", configuration = CustomRibbonConfiguration.class)
public interface OrderService {
    @GetMapping("/order/get")
    String getOrder();
}
