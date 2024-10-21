package com.baoge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务管理
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.baoge.mapper")
public class JobManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobManagerApplication.class, args);
    }
}
