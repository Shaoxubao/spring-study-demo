package com.baoge.service.impl;

import com.baoge.service.ServerServiceDemo2;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.jws.WebService;

@Component
@WebService(name = "ServerServiceDemo2", targetNamespace = "http://service.baoge.com",
        endpointInterface = "com.baoge.service.ServerServiceDemo2")
public class ServerServiceDemo2Impl implements ServerServiceDemo2 {

    @Override
    public String emrService(@WebParam String data) {
        if (null == data || "".equals(data.trim())) {
            return "传入的参数为空";
        }
        System.out.println("===========22222222:" + data);
        return "调用成功2";
    }
}
