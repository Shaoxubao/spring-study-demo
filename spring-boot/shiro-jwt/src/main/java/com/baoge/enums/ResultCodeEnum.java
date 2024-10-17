package com.baoge.enums;

/**
 * @Desc:
 */
public enum ResultCodeEnum {
    SUCCESS("0000", "操作成功"),
    SUCCESS_QUERY("0001", "查询成功"),
    SUCCESS_ADD("0002", "添加成功"),
    SUCCESS_UPDATE("0003", "更新成功"),
    SUCCESS_DELETE("0004", "删除成功"),


    TOKEN_ERROR("1000", "token错误"),
    TOKEN_NULL("1001", "token为空"),
    TOKEN_EXPIRED("1002", "token过期"),
    TOKEN_INVALID("1003", "token无效"),

    USER_ERROR("2000", "用户名密码错误"),
    USER_NOT_EXISTS("2001", "用户不存在"),
    USER_INVALID("2002", "用户无效"),
    USER_EXPIRED("2003", "用户过期"),
    USER_BLOCKED("2004", "用户封禁"),
    USER_PASSWORD_ERROR("2005", "密码错误"),

    PARAM_ERROR("3000", "参数错误"),
    PARAM_NULL("3001", "参数为空"),
    PARAM_FORMAT_ERROR("3002", "参数格式不正确"),
    PARAM_VALUE_INCORRECT("3003", "参数值不正确"),
    PARAM_DUPLICATE("3004", "参数重复"),
    PARAM_CONVERT_ERROR("3005", "参数转化错误"),

    AUTHORITY_ERROR("4000", "权限错误"),
    AUTHORITY_UNAUTHORIZED("4001", "无权限"),

    SERVER_ERROR("5000", "服务器内部错误"),
    SERVER_UNAVAILABLE("5001", "服务器不可用"),


    ;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
