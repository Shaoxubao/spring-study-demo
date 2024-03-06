package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HuaweiGatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(HuaweiGatewayApplication.class, args);
  }
}
