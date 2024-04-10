package com.baoge.model;

public class ResponseData {
    public static final int STATUS_OK = 200;
    public static final int STATUS_IDENTITY_OVERDUE = 10001;
    public static final int STATUS_NO_PERMISSION = 10002;
    public static final int STATUS_REQUEST_FAILED = 10003;

    private int statusCode;
    private Object data;
    private String message;
    private String errorMessage;

    public ResponseData() {
        this.statusCode = STATUS_OK;
        this.data = "";
        this.message = "";
        this.errorMessage = "";
    }

    public ResponseData(int statusCode, Object data, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
        this.errorMessage = "";
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "statusCode=" + statusCode +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
