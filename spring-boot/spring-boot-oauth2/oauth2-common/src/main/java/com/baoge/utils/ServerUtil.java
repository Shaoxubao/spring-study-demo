package com.baoge.utils;

import javax.servlet.http.HttpServletRequest;

public class ServerUtil {
    // 从请求中获取状态码
    public static int getStatusCode(HttpServletRequest request) {
        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            return (int) statusCode;
        } else {
            return 500;
        }
    }

    // 判断字符串是否为空
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    // 验证字符串参数正确性
    public static boolean validateStringParamsSuccess(String... params) {
        for (String parameter : params) {
            if (isEmpty(parameter)) {
                return false;
            }
        }

        return true;
    }

    // 验证对象参数正确性
    public static boolean validateObjectParamsSuccess(Object... params) {
        for (Object parameter : params) {
            if (parameter == null) {
                return false;
            }
        }

        return true;
    }
}