package com.demo.rabbitmq.rocketmq;


import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

import java.util.Scanner;


/**
 * Description：rocketMQ生产者测试类
 *
 * @Author yangshilei
 * @Date 2019-01-22 16:06
 */
public class Producer {
  public static void main(String[] args) throws MQClientException {
    DefaultMQProducer producer = new DefaultMQProducer("my-group");

    producer.setNamesrvAddr("localhost:9876");
    producer.setInstanceName("rmq-instance");
    producer.start();

    try {
      Message message = new Message("demo-topic","demo-tag","自生产者的启动确认消息".getBytes());
      producer.send(message);

      while (true){
        String text = new Scanner(System.in).next();
        Message msg = new Message("demo-topic","demo-tag",text.getBytes());
        SendResult sendResult = producer.send(msg);
      }
    }catch (Exception e){
      System.out.println("生产者发送消息异常"+e);
    }

    producer.shutdown();

  }
}
