package com.baoge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = {"com.baoge.mapper"})
@EnableScheduling
@SpringBootApplication
public class JvmMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(JvmMonitorApplication.class, args);
    }
}
