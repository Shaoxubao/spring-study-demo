package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestTemplateDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestTemplateDemoApplication.class, args);
    }
}