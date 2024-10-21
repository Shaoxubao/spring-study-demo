package com.baoge.common;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;

    private T data;

    private String msg;

    public Result() {
        super();
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        return success("");
    }

    public static <T> Result success(T data) {
        Result<T> result = new Result<>(ResultEnum.ODE_200.getStatus(), ResultEnum.ODE_200.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(ResultEnum resultEnum) {
        return createResult(null, resultEnum.getStatus(), resultEnum.getMsg());
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return createResult(null, code, message);
    }

    public static <T> Result<T> fail(Throwable e) {
        //打印异常栈信息到控制台
        e.printStackTrace();
        return createResult(null, 500, "服务器发生错误");
    }

    private static <T> Result<T> createResult(T data, Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(message);
        return r;
    }
}