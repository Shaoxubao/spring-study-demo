package com.baoge.exception;

import com.baoge.utils.ServerUtil;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainErrorController implements ErrorController {
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) {
        throw new MyException(ServerUtil.getStatusCode(request), "请求失败！");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}