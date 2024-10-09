package com.baoge.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class CustomRibbonConfiguration {
     @Bean
    public IRule randomRule() {
        return new RandomRule(); // 或者其他的IRule实现
    }
}