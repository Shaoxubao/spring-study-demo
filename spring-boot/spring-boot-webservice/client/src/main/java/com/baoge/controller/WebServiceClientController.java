package com.baoge.controller;

import com.baoge.service.InvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceClientController {

    @Autowired
    private InvokeService invokeService;


    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test() {
        invokeService.invokeService_2();
    }
}
