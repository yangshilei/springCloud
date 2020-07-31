package com.demo.rabbitmq.rabbitmq.topic;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 通配符型消息队列
 * *：表示匹配一个单词
 * #：表示匹配多个单词
 */
public class TopicProducer {
    public static void main(String[] args) throws Exception {

        final String exchangeName = "topic_exchange";
        final String DIRECT_TYPE = "topic";
        final String routing_key_sms = "log.sms";
        final String routing_key_email = "log.email";
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
