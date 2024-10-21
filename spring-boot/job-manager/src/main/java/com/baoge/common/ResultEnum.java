package com.baoge.common;

/**
 *
 */
public enum ResultEnum {

    ODE_200(200, ""),
    CODE_400(400, "错误的请求参数"),
    CODE_401(401, "没有登录"),
    CODE_403(403, "没有权限"),
    CODE_405(405, "用户被冻结"),
    CODE_500(500, "内部服务器错误");

    private Integer status;
    private String msg;

    ResultEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
