package com.demo.rabbitmq.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "topic.sms")
public class TopicSmsConsumer {

    @RabbitHandler
    public void process(Map msg){
        System.out.println("短信消费者--->"+msg.toString());
        int i = 1/0; // 该段代码用来测试重试机制；
    }
}
