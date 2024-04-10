package com.baoge.controller;

import com.baoge.exception.MyException;
import com.baoge.model.ResponseData;
import com.baoge.model.params.UserLoginParams;
import com.baoge.model.po.User;
import com.baoge.service.UserService;
import com.baoge.utils.ServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${third-login-redirect-url}")
    private String thirdLoginRedirectUrl;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public ResponseData login(@RequestBody UserLoginParams params) {
        String account = params.account;
        String password = params.password;

        if (!ServerUtil.validateStringParamsSuccess(account, password)) {
            throw new MyException(ResponseData.STATUS_REQUEST_FAILED, "用户名或密码不能为空！");
        }

        User user = userService.validateUser(account, password);
        if (user != null) {
            return new ResponseData(ResponseData.STATUS_OK, user, "登录成功！");
        } else {
            return new ResponseData(ResponseData.STATUS_REQUEST_FAILED, null, "用户不存在！");
        }
    }

    @RequestMapping(value = "/thirdLogin", method = RequestMethod.GET)
    public void thirdLogin(HttpServletResponse response) {
        try {
            //重定向到资源服务器进行身份验证
            response.sendRedirect(thirdLoginRedirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/thirdInfo")
    public ResponseData thirdInfo(Integer userId) {
        if (!ServerUtil.validateObjectParamsSuccess(userId)) {
            throw new MyException(ResponseData.STATUS_REQUEST_FAILED, "用户id不能为空！");
        }

        User user = userService.findThirdUser(userId);
        if (user != null) {
            return new ResponseData(ResponseData.STATUS_OK, user, "获取成功！");
        } else {
            return new ResponseData(ResponseData.STATUS_REQUEST_FAILED, null, "用户不存在！");
        }
    }
}