package com.baoge;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "nacos-demo.yml", autoRefreshed = true)
public class Nacos2ConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(Nacos2ConfigApplication.class, args);
    }
}

