package com.demo.rabbitmq.rabbitmq.fanout;

import com.demo.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author ：yangshilei
 * @Date ：2020/7/25 17:19
 * @Description：扇形交换机消费者
 */
public class EmailConsumer {
    public static void main(String[] args){
        System.out.println("开始短信消费");
        final String exchangeName = "test_exchange";
        final String emailQueue = "email_queue";
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();

            // 建立队列
            channel.queueDeclare(emailQueue,false,false,false,null);
            // 将队列和交换机相互绑定:(队列名，交换机名，绑定策略)
            channel.queueBind(emailQueue,exchangeName,"");

            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body);
                    System.out.println("邮件队列收到的消息==="+message);
                }
            };
            channel.basicConsume(emailQueue,true,defaultConsumer);
        }catch (Exception e){
            System.out.println("邮件消费者消费异常"+e);
        }
    }
}
