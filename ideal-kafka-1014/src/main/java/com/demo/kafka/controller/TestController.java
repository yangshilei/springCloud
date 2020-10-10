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

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
//        properties.load(new FileInputStream("C:\\学习\\计算机技术\\我的项目\\springCloud\\ideal-kafka-1014\\src\\main\\resources\\properties.properties"));
        properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("properties.properties"),"UTF-8"));
        System.out.println(properties.getProperty("name"));
        Enumeration<Object> keys = properties.keys();
        System.out.println(JSONObject.toJSONString(keys));
        System.out.println("=="+properties.get("age"));
    }
}
