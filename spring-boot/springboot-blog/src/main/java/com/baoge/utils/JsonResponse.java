package com.baoge.utils;


/**
 * @ClassName JsonResponse
 **/
public class JsonResponse<T> {
    public static final int SUCCESS_CODE;
    public static final int FAIL_CODE;
    public static final JsonResponse<Void> SUCCESS;
    public static final JsonResponse<Void> FAIL;
    private Integer code;
    private boolean success;
    private String message;
    private T data;

    public JsonResponse() {
        this.code = SUCCESS_CODE;
        this.success = true;
    }

    public JsonResponse(Integer code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public JsonResponse(Integer code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> JsonResponse<T> success(T data) {
        return new JsonResponse(SUCCESS_CODE, ResSuccess.SYS_200.getMessage(), data, true);
    }

    public static <T> JsonResponse<T> success(String message, T data) {
        return new JsonResponse(SUCCESS_CODE, message, data, true);
    }

    public static <T> JsonResponse<T> success(int code, String message, T data) {
        return new JsonResponse(code, message, data, true);
    }

    public static <T> JsonResponse<T> fail(String message) {
        return new JsonResponse(FAIL_CODE, message, false);
    }

    public static <T> JsonResponse<T> fail(int code, String message) {
        return new JsonResponse(code, message, false);
    }

    public static <T> JsonResponse<T> fail(int code, String message, T data) {
        return new JsonResponse(code, message, data, false);
    }

    public static <T> JsonResponse<T> unauthorized() {
        return new JsonResponse(ResError.SYS_401.getCode(), ResError.SYS_401.getMessage(), false);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "JsonResponse{code=" + this.code + ", success=" + this.success + ", message='" + this.message + '\'' + ", data=" + this.data + '}';
    }

    static {
        SUCCESS_CODE = ResSuccess.SYS_200.getCode();
        FAIL_CODE = ResError.SYS_500.getCode();
        SUCCESS = new JsonResponse(ResSuccess.SYS_200.getCode(), ResSuccess.SYS_200.getMessage(), true);
        FAIL = new JsonResponse(FAIL_CODE, "操作失败！", false);
    }
}

