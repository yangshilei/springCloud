package com.demo.rocketmq.simpleTest;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "test-topic",consumerGroup = "my-consumer-group")
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("我收到消息了！消息内容为："+message);
    }
}
