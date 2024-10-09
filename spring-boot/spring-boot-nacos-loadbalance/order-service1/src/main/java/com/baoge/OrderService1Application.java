package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class OrderService1Application {
    public static void main( String[] args ) {
        SpringApplication.run(OrderService1Application.class, args);
    }
}
