package com.demo.rabbitmq.consumers;

import com.demo.rabbitmq.config.DxlQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description: 订单的死信队列消息消费者
 * @Author: yangshilei
 * @Date:
 */
@Component
public class DxlOrderConsumer {

    @RabbitListener(queues = DxlQueueConfig.DXL_QUEUE)
    public void getDxlOrderMsg(String msg){
        System.out.println("死信队列消息="+msg);
    }
}
