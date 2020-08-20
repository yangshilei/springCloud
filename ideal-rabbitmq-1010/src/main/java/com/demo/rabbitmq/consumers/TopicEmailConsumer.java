package com.demo.rabbitmq.consumers;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 该消费者用来测试手动ack功能
 */
@Component
public class TopicEmailConsumer{

    @RabbitListener(queues = "topic.email")
    public void process(Map msg, Message message, Channel channel){
        System.out.println("手动确认消费者方法中"+msg);
        String messageStr = new String(message.getBody());
        System.out.println("获取到的消息"+messageStr);
        try {
            boolean flag = true;// 表示某种业务执行成功
            if(flag){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (Exception e) {
            System.out.println("手动确认收到消息发生异常");
            try {
                //丢弃这条消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                System.out.println("丢弃这条消息发生异常");
                e1.printStackTrace();
            }
        }
    }


}
