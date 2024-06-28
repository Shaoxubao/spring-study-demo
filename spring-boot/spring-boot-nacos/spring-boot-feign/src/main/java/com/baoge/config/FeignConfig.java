package com.baoge.config;

import cn.hutool.core.lang.Snowflake;
import com.baoge.client.MyFeignClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
 
@Configuration
public class FeignConfig {

    static Map<String, String> bizCodeMap = new HashMap<>();

    static {
        bizCodeMap.put("get3", "2050999999");
    }
 
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String url = template.url().substring(template.url().lastIndexOf("/") + 1);
                String bizCode = bizCodeMap.get(url);

                Snowflake snowflak = new Snowflake(1L, 1L);
                String cmdId = snowflak.nextIdStr();
                String sessionId = bizCode + "20240628" + cmdId.substring(cmdId.length() - 12);
                template.header("token", "328943593483ldsklksl");
                template.header("sessionId", sessionId);
                template.header("accessCode", "205099"); // 网元编码
                template.header("bizCode", bizCode); // 业务编码
            }
        };
    }
}