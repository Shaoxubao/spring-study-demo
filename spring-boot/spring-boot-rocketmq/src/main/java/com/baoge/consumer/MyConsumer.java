package com.baoge.consumer;

import com.baoge.constant.Constants;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
 
@Component
@RocketMQMessageListener(topic = Constants.DEMO_TOPIC, consumerGroup = Constants.ROCKETMQ_CONSUMER_GROUP)
public class MyConsumer implements RocketMQListener<String> {
 
    @Override
    public void onMessage(String message) {
        // 处理消息的逻辑
        System.out.println("Received message: " + message);
    }
}