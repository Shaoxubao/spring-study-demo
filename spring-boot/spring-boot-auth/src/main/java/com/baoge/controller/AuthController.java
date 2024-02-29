package com.baoge.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("")
public class AuthController {

    @RequestMapping("/auth")
    public ResponseEntity<String> auth(@RequestHeader(required = false, value = "Authorization") String authorization,
                                       HttpServletResponse response) {
        if (StringUtils.isBlank(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"code\": 401, \"msg\": \"未授权!\"}");
        }
        // 设置响应头
        response.setHeader("X-User-ID", "test=======");
        System.out.println("auth");
        return ResponseEntity.status(HttpStatus.OK).body("{\"code\": 200, \"msg\": \"成功!\"}");
    }

    @RequestMapping("/test")
    public void test() {
        System.out.println("test");
    }
}
