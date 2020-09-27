package com.demo.kafka.consumer;

import com.demo.kafka.dto.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 测试kafka消息传递
 */
@Component
@Slf4j
public class TestTopicConsumer {
    String topic = KafkaTopic.ONT.getMsg();

    @KafkaListener(topics = "demo")
    public void testTopicComsumer(ConsumerRecord<?, ?> record){
      log.info("进入testTopicConsumer消费者方法中");
        Object key = record.key();
        String topic = record.topic();
        Object value = record.value();
        int partition = record.partition();
        log.info("topic==={}",topic);
        log.info("value==={}",value);
        log.info("partition==={}",partition);
    }
}
