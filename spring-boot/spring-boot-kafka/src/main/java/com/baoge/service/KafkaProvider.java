package com.baoge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class KafkaProvider {
    /**
     * 消息 TOPIC
     */
    private static final String TOPIC = "shopping";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage() {

        // 发送消息，订单类的 json 作为消息体
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(TOPIC, "hello");

        // 监听回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("## Send message fail ...");
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("## Send message success ...");
            }
        });
    }
}

