package com.demo.rabbitmq.controller;

import com.demo.rabbitmq.config.DxlQueueConfig;
import com.demo.rabbitmq.config.TopicRabbitMqConfig;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Description：
 *
 * @Author yangshilei
 * @Date 2019-01-09 20:13
 */
@Api(tags = "测试模块")
@RestController
@Slf4j
@RequestMapping("test")
public class TestController {


  /**
   * rabbitMQ测试接口
   * @return
   */
  @ApiOperation(value = "测试接口", notes = "测试接口")
  @PostMapping(value = "/ideal/test" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  String test(@RequestParam("name") String name){
    log.info("进入测试接口！");
    return "hello world" + name;
  }



}
