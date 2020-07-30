package com.demo.rabbitmq.rabbitmq.direct;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 路由生产者模式
 */
public class DirectProducer {
    public static void main(String[] args) throws Exception {
        final String exchangeName = "direct_exchange";
        final String DIRECT_TYPE = "direct";
        final String routing_key_sms = "sms";
        final String routing_key_email = "email";
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName,DIRECT_TYPE);

        String smsMsg = "sms message";
        channel.basicPublish(exchangeName,routing_key_sms,null,smsMsg.getBytes());
        System.out.println("短信消息发送成功");

        String emailMsg = "email message";
        channel.basicPublish(exchangeName,routing_key_email,null,emailMsg.getBytes());
        System.out.println("邮件消息发送成功");
        channel.close();
        connection.close();
    }
}
