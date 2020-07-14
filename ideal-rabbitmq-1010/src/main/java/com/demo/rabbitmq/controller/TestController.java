package com.demo.rabbitmq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description：
 *
 * @Author yangshilei
 * @Date 2019-01-09 20:13
 */
@Api(tags = "rabbitMQ消息分发模块")
@RestController
@Slf4j
@RequestMapping("test")
public class TestController {

  /**
   * rabbitMQ测试接口
   * @return
   */
  @ApiOperation(value = "rabbitMQ测试接口", notes = "rabbitMQ测试接口")
  @PostMapping(value = "/ideal/rabbitmq/test" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  String test(@RequestParam("name") String name){
    log.info("进入rabbitMQ测试接口！");
    return "hello world" + name;
  }
}
