package com.baoge.utils;

public enum ResError {
    SYS_400(400, "无效的请求", ""),
    SYS_401(401, "无权限操作", "请联系在技术支持进行授权。"),
    SYS_402(402, "未找到服务地址", "确认REST服务是否注册，建议通过接口测试页面进行验证。"),
    SYS_403(403, "rest service invoke error", "确认REST服务是否注册，建议通过接口测试页面进行验证。"),
    SYS_420(420, "缺少参数%s", ""),
    SYS_424(424, "%s参数重复。", ""),
    SYS_425(425, "timeout参数非法。", ""),
    SYS_426(426, "请求参数的值与类型不匹配。", ""),
    SYS_500(500, "Internal Server Error", ""),
    SYS_504(504, "请求超时", "");

    private int code;
    private String message;
    private String solution;

    private ResError(int code, String message, String solution) {
        this.code = code;
        this.message = message;
        this.solution = solution;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.code + ":" + this.message + "  解决方案：" + this.solution;
    }

    public String getSolution() {
        return this.solution;
    }
}

