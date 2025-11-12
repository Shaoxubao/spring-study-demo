package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Sm4DemoApplication {
    
    public static void main(String[] args) {
        // 设置BouncyCastle提供者
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        SpringApplication.run(Sm4DemoApplication.class, args);
    }
}