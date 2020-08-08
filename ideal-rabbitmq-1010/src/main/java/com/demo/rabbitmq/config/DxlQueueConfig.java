package com.demo.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DxlQueueConfig {

    public static final String ORDER_QUEUE = "order.normal";

    public static final String DXL_QUEUE = "order.dxl";

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String DXL_EXCHANGE = "dxl.exchange";

    public static final String DXL_ROUTE = "dxl";

    public static final String ORDER_ROUTE= "order.*";

    // 定义一个死信队列和交换机，并建立绑定关系
    @Bean
    public Queue getDxlQueue(){
        return new Queue(DXL_QUEUE,true);
    }

    @Bean
    public  TopicExchange getDxlExchange(){
        return new TopicExchange(DXL_EXCHANGE);
    }

    @Bean
    Binding bindDxlQueue2DxlExchange(Queue getDxlQueue,TopicExchange getDxlExchange){
        return BindingBuilder.bind(getDxlQueue).to(getDxlExchange).with(DXL_ROUTE);
    }

    // 定义一个正常的订单队列和交换机，并绑定关系
    @Bean
    public TopicExchange getOrderExchange(){
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue getOrderQueue(){
        Map<String,Object> map = new HashMap<>(2);
        map.put("x-dead-letter-exchange",DXL_EXCHANGE);
        map.put("x-dead-letter-routing-key",DXL_ROUTE);
        return new Queue(ORDER_QUEUE,true,false,false,map);
    }

    @Bean
    Binding bindOrderQueue2OrderExchange(Queue getOrderQueue,TopicExchange getOrderExchange){
        return BindingBuilder.bind(getOrderQueue).to(getOrderExchange).with(ORDER_ROUTE);
    }


}
