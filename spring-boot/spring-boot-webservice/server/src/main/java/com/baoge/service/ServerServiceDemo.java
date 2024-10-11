package com.baoge.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ServerServiceDemo", targetNamespace = "http://service.baoge.com")
public interface ServerServiceDemo {
    @WebMethod
    String emrService(@WebParam String data);
 
}