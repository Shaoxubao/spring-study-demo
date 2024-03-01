package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDiscoveryClient
//引入了spring-cloud-starter-alibaba-nacos-discovery依赖时，默认情况下会自动启用Nacos的服务注册和发现功能
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class,args);
    }
}
