package com.demo.rabbitmq.consumers;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 该消费者用来测试rabbitMQ消息重试的配置；
 */
@Component
@RabbitListener(queues = "topic.sms")
public class TopicSmsConsumer {

    @RabbitHandler
    public void process(Map msg, Message message, Channel channel) throws IOException {
        System.out.println("短信消费者--->"+msg.toString());

        // 由于配置了手动确认消息机制，所有消费者必须要手动确认消费消息；
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        int i = 1/0; // 该段代码用来测试重试机制；
    }
}
