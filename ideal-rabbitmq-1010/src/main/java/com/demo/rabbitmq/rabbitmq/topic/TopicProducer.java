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

        try {
            channel.txSelect();// 开启手动提交事务
            String smsMsg = "sms message";
            channel.basicPublish(exchangeName,routing_key_sms,null,smsMsg.getBytes());
            channel.txCommit();// 提交事务
            System.out.println("消息提交结束");
        }catch (Exception e){
            channel.txRollback();// 有异常时候回滚
            System.out.println("程序异常，发送的队列消息回滚");
        }


        String emailMsg = "email message";
        channel.basicPublish(exchangeName,routing_key_email,null,emailMsg.getBytes());
        System.out.println("邮件消息发送成功");
        channel.close();
        connection.close();
    }
}
