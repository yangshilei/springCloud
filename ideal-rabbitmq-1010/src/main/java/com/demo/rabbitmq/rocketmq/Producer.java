package com.demo.rabbitmq.rocketmq;


import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.BrokerConfig;
import com.alibaba.rocketmq.common.message.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;


/**
 * Description：rocketMQ生产者测试类
 *
 * @Author yangshilei
 * @Date 2019-01-22 16:06
 */
@Slf4j
public class Producer {
  public static void main(String[] args) throws MQClientException {
//    final BrokerConfig brokerConfig = new BrokerConfig();
//    brokerConfig.setNamesrvAddr("localhost:9876");

    DefaultMQProducer producer = new DefaultMQProducer("my-group");
    producer.setNamesrvAddr("localhost:9876");
//    producer.setInstanceName("rmq-instance");
    producer.start();

    try {

      Message message = new Message("demo-topic","demo-tag","自生产者的启动确认消息".getBytes());
      producer.send(message);

    }catch (Exception e){
      log.info("===={}",e);
    }

    producer.shutdown();

  }
}
