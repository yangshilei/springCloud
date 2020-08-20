package com.demo.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitMqConfig {

    private static final String SMS_QUEUE = "topic.sms";

    private static final String EMAIL_QUEUE = "topic.email";

    public static final String CONFIG_EXCHANGE = "config_exchange";

    // 1.定义短信和邮件队列,消息持久化
    @Bean
    public Queue configSmsQueue(){
        return new Queue(SMS_QUEUE,true);
    }
    @Bean
    public Queue configEmailQueue(){
        return new Queue(EMAIL_QUEUE,true);
    }

    // 2.定义一个交换机
    @Bean
    public TopicExchange configExchange(){
        return new TopicExchange(CONFIG_EXCHANGE);
    }

    // 3.将队列绑定到交换机上
    @Bean
    Binding bindingExchangeSms(Queue configSmsQueue,TopicExchange configExchange){
        return BindingBuilder.bind(configSmsQueue).to(configExchange).with("topic.sms");
    }

    @Bean
    Binding bindingExchangeEmail(Queue configEmailQueue,TopicExchange configExchange){
        // 邮件交换机绑定topic.*路由，可以同时接受短信和邮件的消息
        return BindingBuilder.bind(configEmailQueue).to(configExchange).with("topic.*");
    }

}
