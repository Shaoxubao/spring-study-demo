package com.baoge.common;

/**
 *
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int status;
    private String msg;
    private Object obj;

    public BusinessException() {
    }

    public BusinessException(String msg) {
        this.status = ResultEnum.CODE_400.getStatus();
        this.msg = msg;
    }

    public BusinessException(String msg, Object obj) {
        this.status = ResultEnum.CODE_400.getStatus();
        this.msg = msg;
        this.obj = obj;
    }

    public BusinessException(int status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }

    public BusinessException(ResultEnum resultEnum) {
        this.status = resultEnum.getStatus();
        this.msg = resultEnum.getMsg();
    }

    public BusinessException(ResultEnum resultEnum, Object obj) {
        this.status = resultEnum.getStatus();
        this.msg = resultEnum.getMsg();
        this.obj = obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getObj() {
        return obj;
    }
}
