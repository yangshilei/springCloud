package com.demo.rabbitmq.rabbitmq.fanout;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author ：yangshilei
 * @Date ：2020/7/25 15:47
 * @Description：扇形交换机生产者：适用于群发通知消息
 */
public class FanOutProducer {

    public static void main(String[] args){
        final String exchangeName = "test_exchange";
        final String FANOUT_TYPE = "fanout";
        Connection connection = null;
        Channel channel = null;
        try {
            // 1.创建连接
            connection = ConnectionUtil.getConnection();

            // 2.创建通道
            channel = connection.createChannel();

            // 3.创建扇形交换机
            channel.exchangeDeclare(exchangeName,FANOUT_TYPE);

            // 4.创建消息
            String message = "this is a fanout test message";

            // 5.发送消息
            channel.basicPublish(exchangeName,"",null,message.getBytes());
            System.out.println("消息发送结束");
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                channel.close();
                connection.close();
            } catch (IOException e) {
                System.out.println("连接关闭异常"+e);
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }
    }
}
