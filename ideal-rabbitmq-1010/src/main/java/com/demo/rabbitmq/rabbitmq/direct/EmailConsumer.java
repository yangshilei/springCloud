package com.demo.rabbitmq.rabbitmq.direct;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class EmailConsumer {
    public static void main(String[] args) throws Exception {
        System.out.println("进入邮件消费者方法");
        final String exchangeName = "direct_exchange";
        final String queueName = "email_queue";
        final String routing_key_email = "email";
        final String routing_key_sms = "sms";
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName,false,false,false,null);

        // 给队列添加一个“email”和“sms”的路由，
        channel.queueBind(queueName,exchangeName,routing_key_email);
        channel.queueBind(queueName,exchangeName,routing_key_sms);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("邮件队列收到的消息==="+message);
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //  采用手动应答模式响应取消息结果
        channel.basicConsume(queueName,false,defaultConsumer);
    }
}
