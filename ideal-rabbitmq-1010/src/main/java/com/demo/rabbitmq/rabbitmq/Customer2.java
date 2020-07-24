package com.demo.rabbitmq.rabbitmq;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 工作队列（公平队列消费者）
 */
public class Customer2 {

    private final static String QUEUE_NAME = "hello world";

    public static void main(String[] argv) throws Exception {

        System.out.println("进入消费者方法");
        // 1、获取到连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明一个队列：名称、持久性的（重启仍存在此队列）、非私有的、非自动删除的
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);// 配置公平消费的qos；

        System.out.println("等待接收消息");

        /* 定义消费者 */
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接收到的消息=" + message);

                // 因为各业务模块执行时间不等，所以采用手动应答模式，
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        // 采用工作队列模式，此处设置为手动应答模式，能保证那个模块先处理完，就能再此优先获得队列中的数据；
        channel.basicConsume(QUEUE_NAME, false, consumer);
        System.out.println("工作队列结束");
    }

}
