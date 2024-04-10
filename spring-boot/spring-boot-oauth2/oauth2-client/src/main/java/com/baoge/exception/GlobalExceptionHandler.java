package com.baoge.exception;

import com.baoge.model.ResponseData;
import com.baoge.utils.ServerUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseData runTimeErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        // TODO 日志
        e.printStackTrace();

        ResponseData responseData = new ResponseData();
        responseData.setStatusCode(ServerUtil.getStatusCode(request));
        responseData.setMessage("访问失败！");
        responseData.setErrorMessage(e.toString());

        return responseData;
    }

    @ExceptionHandler(value = MyException.class)
    public ResponseData requestErrorHandler(MyException e) throws Exception {
        ResponseData responseData = new ResponseData();
        responseData.setStatusCode(e.getStatusCode());
        responseData.setMessage(e.getMessage());

        return responseData;
    }
}