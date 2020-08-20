package com.demo.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 
 * @Description:  配置消息确认回调函数
 * @Author: yangshilei
 * @Date: 
 */
@Slf4j
@Configuration
public class MsgConfirmConfig {

    // 前提条件是yml配置文件中已经配置了
    //   #确认消息已发送到交换机(Exchange)
    //    publisher-confirms: true
    //    #确认消息已发送到队列(Queue)
    //    publisher-returns: tru
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("ConfirmCallback:     "+"相关数据："+correlationData);
                log.info("ConfirmCallback:     "+"确认情况："+ack);
                log.info("ConfirmCallback:     "+"原因："+cause);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            // 在发送到交换机但找不到队列的时候，会打印出改行的日志信息
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("ReturnCallback:     "+"消息："+message);
                log.info("ReturnCallback:     "+"回应码："+replyCode);
                log.info("ReturnCallback:     "+"回应信息："+replyText);
                log.info("ReturnCallback:     "+"交换机："+exchange);
                log.info("ReturnCallback:     "+"路由键："+routingKey);
            }
        });

        return rabbitTemplate;
    }

}
