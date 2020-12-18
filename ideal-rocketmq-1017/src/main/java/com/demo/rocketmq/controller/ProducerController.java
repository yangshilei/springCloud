package com.demo.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.demo.rocketmq.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 监听消息进行消费
 */
@Slf4j
@RestController
public class ProducerController {
    @Autowired
    private DefaultMQProducer defaultProducer;

    @Autowired
    private TransactionMQProducer transactionProducer;


    /**
     * 发送普通消息
     */
    @GetMapping("/sendMessage")
    public void sendMsg() {
        log.info("进入发送普通消息方法，发送100个用户==={}",defaultProducer.toString());
        User user = new User();
        for(int i=0;i<100;i++){
            user.setId(String.valueOf(i));
            user.setUsername("yangshilei"+i);
            String json = JSON.toJSONString(user);
            Message msg = new Message("user-topic","white",json.getBytes());
            try {
                SendResult result = defaultProducer.send(msg);
                log.info("发送次数{}:消息id={}:发送状态={}",i,result.getMsgId(),result.getSendStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 支持顺序发送消息
     */
    @GetMapping("/sendMessOrder")
    public void sendMsgOrder() {
        log.info("进入支持顺序发送消息发送消息方法");
        User user = new User();
        for(int i=0;i<100;i++) {
            user.setId(String.valueOf(i));
            user.setUsername("jhp" + i);
            String json = JSON.toJSONString(user);
            Message msg = new Message("user-topic", "white", json.getBytes());
            try{
                defaultProducer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        int index = ((Integer) arg) % mqs.size();
                        return mqs.get(index);
                    }
                },i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
