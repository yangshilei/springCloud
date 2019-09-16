package com.demo.rabbitmq.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Description：
 *
 * @Author yangshilei
 * @Date 2019-01-22 17:50
 */
public class Consumer1 {
  public static void main(String[] args) throws MQClientException {
    System.out.println(22);
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-group");

    consumer.setNamesrvAddr("localhost:9876");
    consumer.setInstanceName("rmq-instance");
    // 消费者订阅的topic消息名
    consumer.subscribe("demo-topic","demo-tag");

    // 注册消息侦听
    consumer.registerMessageListener(new MessageListenerConcurrently() {
      @Override
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        for(MessageExt msg: msgs){
          byte[] body = msg.getBody();
          String s = new String(body);
          System.out.println("消费者1获得的消息:"+ s);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
    });

    consumer.start();
    System.out.println("消费者开始！");
  }
}
