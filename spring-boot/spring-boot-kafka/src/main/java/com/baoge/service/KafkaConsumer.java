package com.baoge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @create 2022-10-08 1:25
 * @describe 通过指定的话题和分组来消费对应的话题
 */
@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "shopping", groupId = "group_id") // 这个groupId是在yml中配置的
    public void consumer(String message) {
        log.info("## consumer message: {}", message);
    }
}
