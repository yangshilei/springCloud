package com.demo.rabbitmq.controller;

import com.demo.rabbitmq.config.DxlQueueConfig;
import com.demo.rabbitmq.config.TopicRabbitMqConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:  rabbit相关的测试接口
 * @Author: yangshilei
 * @Date:
 */
@Api(tags = "rabbitMQ消息分发模块")
@RestController
@Slf4j
@RequestMapping("rabbit")
public class RabbitController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @ApiOperation(value = "MQ生产者发送短信相关消息", notes = "MQ生产者发送短信相关消息")
    @GetMapping("/producer/sms/send")
    String producerSmsSend(){
        log.info("进入SMS生产者方法");
        String uuid = String.valueOf(UUID.randomUUID());
        String msg = "sms message";
        Map<String,String> map = new HashMap<String,String>();
        map.put("uuid",uuid);
        map.put("message",msg);

        // 开始发送消息
        log.info("开始发送sms消息");
        rabbitTemplate.convertAndSend(TopicRabbitMqConfig.CONFIG_EXCHANGE,"topic.sms",map);
        log.info("消息发送成功!");
        return "success";
    }

    @ApiOperation(value = "MQ生产者发送邮件相关消息", notes = "MQ生产者发送邮件相关消息")
    @GetMapping("/producer/email/send")
    String producerEmailSend(){
        log.info("进入email生产者方法");
        String uuid = String.valueOf(UUID.randomUUID());
        String msg = "this is email message";
        Map<String,String> map = new HashMap<String,String>();
        map.put("uuid",uuid);
        map.put("message",msg);

        // 开始发送消息
        log.info("开始发送email消息");
        rabbitTemplate.convertAndSend(TopicRabbitMqConfig.CONFIG_EXCHANGE,"topic.email",map);
        log.info("消息发送成功!");
        return "success";
    }

    @ApiOperation(value = "死信队列测试消息发送", notes = "死信队列测试消息发送")
    @GetMapping("/producer/dxl/send")
    String sendDxlMsg(){
        log.info("进入死信测试消息发送方法");
        String meg = "dxl message!!!";
        rabbitTemplate.convertAndSend(DxlQueueConfig.ORDER_EXCHANGE, DxlQueueConfig.ORDER_ROUTE, meg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");// 发送的消息设定定时时间10秒；
                return message;
            }
        });
        log.info("死信测试消息发送成功");
        return "success";
    }

//    ①消息推送到server，但是在server里找不到交换机
//    ②消息推送到server，找到交换机了，但是没找到队列
//    ③消息推送到sever，交换机和队列啥都没找到
//    ④消息推送成功
    @ApiOperation(value = "消息推到server，但server无交换机", notes = "消息推到server，但server无交换机")
    @GetMapping("/TestMessageAck")
    public String TestMessageAck() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
        return "ok";
    }
}
