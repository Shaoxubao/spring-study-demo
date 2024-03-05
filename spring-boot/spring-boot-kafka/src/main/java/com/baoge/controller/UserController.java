package com.baoge.controller;

import com.baoge.service.KafkaProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private KafkaProvider kafkaProvider;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
        kafkaProvider.sendMessage();
    }

}
