package com.baoge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

  @Value("${server.port}")
  private String port;

  @Value("${app.name}")
  private String appName;

  @GetMapping("/sayHello")
  public String sayHello(@RequestParam("name") String name) {
    return "Hello " + name + ", This is Spring cloud Huawei + CSE!";
  }

  @GetMapping("/test")
  public String test() {
    System.out.println("port: " + port + ", appName: " + appName);
    return "Hello, This is Spring cloud Huawei + CSE!";
  }

}
