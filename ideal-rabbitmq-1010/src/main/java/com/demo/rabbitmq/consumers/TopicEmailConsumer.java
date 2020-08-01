package com.demo.rabbitmq.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "topic.email")
public class TopicEmailConsumer {

    @RabbitHandler
    public void process(Map msg){
        System.out.println("邮件消费者--->"+msg.toString());
    }
}
