package com.baoge.config;

import com.baoge.interceptor.YcRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 配置类
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private YcRequestInterceptor ycRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ycRequestInterceptor);
    }
}