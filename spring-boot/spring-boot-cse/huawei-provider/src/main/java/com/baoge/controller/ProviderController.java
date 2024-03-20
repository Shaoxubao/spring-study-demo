package com.baoge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

  @GetMapping("/sayHello")
  public String sayHello(@RequestParam("name") String name) {
    return "Hello " + name + ", This is Spring cloud Huawei + CSE!";
  }

  @GetMapping("/test")
  public String test() {
    return "Hello, This is Spring cloud Huawei + CSE!";
  }

}
