package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Nacos2xConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nacos2xConfigApplication.class, args);
    }
}