package com.demo.kafka.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.kafka.dto.KafkaTopic;
import com.demo.kafka.dto.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

@RestController
@Api("kafka测试接口")
@RequestMapping("/kafka")
@Slf4j
public class TestController {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @PostMapping("/test")
    Result kafkaTest(){
        log.info("进入kafka生产者测试方法");
        kafkaTemplate.send(KafkaTopic.ONT.getMsg(),"hello world");
        return Result.ok("测试成功");
    }

}
