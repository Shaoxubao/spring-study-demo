package com.baoge.interceptor;

import com.baoge.client.MyFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 拦截器
@Component
public class YcRequestInterceptor implements HandlerInterceptor {

    @Autowired
    private MyFeignClient myFeignClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在请求处理之前，调用其他服务
        System.out.println("拦截器调用结果：" + myFeignClient.getOrder1());
        return true;
    }
}