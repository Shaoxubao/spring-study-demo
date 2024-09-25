package com.baoge.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("config")
public class ConfigController {

    @NacosValue(value = "${blog.name}", autoRefreshed = true)
    private String useLocalCache;

    @GetMapping("/get")
    @ResponseBody
    public String get() {
        return useLocalCache;
    }
}