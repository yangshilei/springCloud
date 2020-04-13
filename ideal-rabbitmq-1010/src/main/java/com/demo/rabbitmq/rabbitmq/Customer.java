package com.demo.rabbitmq.rabbitmq;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;
import java.io.IOException;

public class Customer {

    private final static String QUEUE_NAME = "simple_queue";

    public static void main(String[] argv) throws Exception {

        // 1、获取到连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明一个队列：名称、持久性的（重启仍存在此队列）、非私有的、非自动删除的
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("Waiting for messages.");

        /* 定义消费者 */
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接收到的消息====" + message);
            }
        };

        // 将消费者绑定到队列，并设置自动确认消息（即无需显示确认，如何设置请慎重考虑）
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
