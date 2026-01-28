1、下载cse解压，启动cse.exe，访问 http://127.0.0.1:30103 可看到 CSE 管理台页面
2、分别搭建huawei-provider、huawei-consumer 和 huawei-gateway
3、分别启动以上服务
4、访问 http://localhost:9090/sayHello?name=huawei
5、使用配置中心
   在控制台配置管理-配置列表-创建配置项，选微服务级配置，配置项唯一标识如：com.baoge
   配置格式选yaml，配置内容如：
   server:
     port: 9091
   ...
   项目配置文件中添加如下配置：
   ```
   server:
      port: 8010
    
    spring:
      application:
        name: huawei-provider
      cloud:
        servicecomb:
          discovery:
            enabled: true
            watch: false
            address: http://10.238.49.231:30100
            appName: hauweicloud-application
            serviceName: ${spring.application.name}
            version: 0.0.1
          config:
            enabled: true
            serverType: kie
            serverAddr: http://10.238.49.231:30110
            fileSource: com.baoge.yaml  （配置项唯一标识，重点）

   ```
