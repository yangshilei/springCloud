package com.demo.rocketmq.rocketmq;

import com.demo.rocketmq.dto.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

//@EnableConfigurationProperties(RocketMQProperties.class)
@Configuration
@Slf4j
public class RocketMQConfiguration {

    @Autowired
    private RocketMQProperties rocketMQProperties;

    // 事件监听
    @Autowired
    private ApplicationEventPublisher publisher = null;

    private static boolean isFirstSub = true;

    private static long startTime = System.currentTimeMillis();

    /**
     * 容器初始化的时候 打印参数
     */
    @PostConstruct
    public void init(){
        System.out.println("初始化"+rocketMQProperties.getNamesrvAddr());
        System.out.println("初始化"+rocketMQProperties.getProducerGroupName());
        System.out.println("初始化"+rocketMQProperties.getConsumerBatchMaxSize());
        System.out.println("初始化"+rocketMQProperties.getConsumerGroupName());
        System.out.println("初始化"+rocketMQProperties.getConsumerInstanceName());
        System.out.println("初始化"+rocketMQProperties.getProducerInstanceName());
        System.out.println("初始化"+rocketMQProperties.getProducerTranInstanceName());
        System.out.println("初始化"+rocketMQProperties.getTransactionProducerGroupName());
        System.out.println("初始化"+rocketMQProperties.isConsumerBroadcasting());
        System.out.println("初始化"+rocketMQProperties.isEnableHistoryConsumer());
        System.out.println("初始化"+rocketMQProperties.isEnableOrderConsumer());
        System.out.println("初始化"+rocketMQProperties.getSubscribe().get(0));
    }

    /**
     * 创建支持消息事务发送的实例
     * @return
     * @throws MQClientException
     */
    @Bean
    public DefaultMQProducer transactionProducer()throws MQClientException{
        TransactionMQProducer producer = new TransactionMQProducer(rocketMQProperties.getTransactionProducerGroupName());
        producer.setInstanceName(rocketMQProperties.getProducerTranInstanceName());
        producer.setNamesrvAddr(rocketMQProperties.getNamesrvAddr());
        producer.setRetryTimesWhenSendAsyncFailed(10);
//        // 事务回查最小并发数
//        producer.setCheckThreadPoolMinSize(2);
//        // 事务回查最大并发数
//        producer.setCheckThreadPoolMaxSize(2);
//        // 队列数
//        producer.setCheckRequestHoldMax(2000);
        producer.start();
        log.info("支持事务消息的实例创建完成....");
        return producer;
    }

    /**
     * 创建消息消费的实例，自动推送的时候将bean注解放开即可
     * @return
     * @throws MQClientException
     */
//    @Bean
    public DefaultMQPushConsumer pushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
                rocketMQProperties.getConsumerGroupName());
        consumer.setNamesrvAddr(rocketMQProperties.getNamesrvAddr());
        consumer.setInstanceName(rocketMQProperties.getConsumerInstanceName());

        //判断是否是广播模式
        if (rocketMQProperties.isConsumerBroadcasting()) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        //设置批量消费
        consumer.setConsumeMessageBatchMaxSize(rocketMQProperties
                .getConsumerBatchMaxSize() == 0 ? 1 : rocketMQProperties
                .getConsumerBatchMaxSize());

        //获取topic和tag
        List<String> subscribeList = rocketMQProperties.getSubscribe();
        for (String sunscribe : subscribeList) {
            consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
        }

        // 顺序消费
        if (rocketMQProperties.isEnableOrderConsumer()) {
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(
                        List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    try {
                        context.setAutoCommit(true);
                        msgs = filterMessage(msgs);
                        if (msgs.size() == 0)
                            return ConsumeOrderlyStatus.SUCCESS;
                        publisher.publishEvent(new MessageEvent(msgs, consumer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
        }  else {
            // 并发消费
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(
                        List<MessageExt> msgs,
                        ConsumeConcurrentlyContext context) {
                    try {
                        //过滤消息
                        msgs = filterMessage(msgs);
                        if (msgs.size() == 0)
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        publisher.publishEvent(new MessageEvent(msgs, consumer));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);

                    try {
                        consumer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    log.info("rocketmq consumer server is starting....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return consumer;
    }

    /**
     * 消息过滤
     * @param msgs
     * @return
     */
    private List<MessageExt> filterMessage(List<MessageExt> msgs) {
        if (isFirstSub && !rocketMQProperties.isEnableHistoryConsumer()) {
            msgs = msgs.stream()
                    .filter(item -> startTime - item.getBornTimestamp() < 0)
                    .collect(Collectors.toList());
        }
        if (isFirstSub && msgs.size() > 0) {
            isFirstSub = false;
        }
        return msgs;
    }

}
