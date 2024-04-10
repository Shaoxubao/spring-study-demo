package com.baoge.exception;

public class MyException extends RuntimeException {
    private int statusCode;
    private String message;

    public MyException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return super.toString() + ", MyException{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}