package com.demo.rabbitmq.rabbitmq.work;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 简单队列模式
 */
public class Customer1 {

    private final static String QUEUE_NAME = "hello world";

    public static void main(String[] argv) throws Exception {

        System.out.println("进入消费者方法");
        // 1、获取到连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明一个队列：名称、持久性的（重启仍存在此队列）、非私有的、非自动删除的
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println("等待接收消息");

        /* 定义消费者 */
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接收到的消息=" + message);

                // 如果是手动应答模式，必须手动应答，否则队列中持久化的消息会一致存在；
//                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        // 自动应答模式（不关注执行结果成功与否），设置为true
        // 手动应答模式（执行成功以后才能返回ack），设置为false
        channel.basicConsume(QUEUE_NAME, true, consumer);
        System.out.println("简单队列结束");
    }

}
