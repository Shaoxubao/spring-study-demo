package com.baoge.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * webservice的接口
 * @WebService(
 * name = "ServerServiceDemo",                  // 暴露服务名称
 * targetNamespace = "http://service.baoge.com" // 命名空间,一般是接口的包名倒序
 * )
 */
@WebService(name = "ServerServiceDemo", targetNamespace = "http://service.baoge.com")
public interface ServerServiceDemo {
    @WebMethod
    String emrService(@WebParam String data);
 
}