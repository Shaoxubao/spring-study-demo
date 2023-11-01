package com.baoge;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = {"com.baoge.mapper"})
public class MultiDatasourceAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceAnnotationApplication.class, args);
    }

}
