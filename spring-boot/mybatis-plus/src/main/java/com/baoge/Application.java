package com.baoge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
@MapperScan(basePackages = {"com.baoge.mapper"})
@SpringBootApplication
@EnableScheduling
@ServletComponentScan(basePackages = {"com.baoge.filter"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
