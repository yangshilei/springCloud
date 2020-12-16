//package com.demo.rocketmq.simpleTest;
//
//import io.swagger.annotations.Api;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Api
//@RequestMapping("/test")
//@RestController
//public class Producer {
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    @GetMapping("send")
//    public void send(){
//        rocketMQTemplate.convertAndSend("test-topic","你好,Java旅途");
//    }
//
//}
