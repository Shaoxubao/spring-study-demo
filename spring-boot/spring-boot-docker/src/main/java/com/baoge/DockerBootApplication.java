package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.baoge.mapper")
public class DockerBootApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DockerBootApplication.class, args);
    }

}
