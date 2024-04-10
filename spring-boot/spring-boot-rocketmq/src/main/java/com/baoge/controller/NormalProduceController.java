package com.baoge.controller;

import com.baoge.constant.Constants;
import lombok.Setter;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NormalProduceController {

    @Setter(onMethod_ = @Autowired)
    private RocketMQTemplate rocketmqTemplate;

    @GetMapping("/testSendMsg")
    public void testSendMsg() {
        Message<String> msg = MessageBuilder.withPayload("Hello, RocketMQ").build();
        rocketmqTemplate.send(Constants.DEMO_TOPIC, msg);
    }
}