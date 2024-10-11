package com.baoge.service;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Service;

@Service
public class InvokeService {
    /**
     * 1.代理类工厂的方式,需要拿到对方的接口地址, 同时需要引入接口
     */
//    public void invokeService_1() {
//        // 接口地址
//        String address = "http://localhost:8080/services/ws/api?wsdl";
//        // 代理工厂
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
//        // 设置代理地址
//        jaxWsProxyFactoryBean.setAddress(address);
//        // 设置接口类型
//        jaxWsProxyFactoryBean.setServiceClass(ServerServiceDemo.class);
//        // 创建一个代理接口实现
//        ServerServiceDemo us = (ServerServiceDemo) jaxWsProxyFactoryBean.create();
//        // 数据准备
//        String data = "hello world";
//        // 调用代理接口的方法调用并返回结果
//        String result = us.emrService(data);
//        System.out.println("返回结果:" + result);
//    }

    /**
     * 2. 动态调用
     */
    public void invokeService_2() {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:19000/services/ws/api?wsdl");
        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            //这里注意如果是复杂参数的话，要保证复杂参数可以序列化
            objects = client.invoke("emrService", "hello world");
            System.out.println("返回数据:" + objects[0]);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
