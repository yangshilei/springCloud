package com.demo.kafka.controller;

import com.demo.kafka.dto.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("kafka测试接口")
@RequestMapping("/kafka")
public class TestController {

    @PostMapping("/test")
    Result kafkaTest(){
        return Result.ok("测试成功");
    }

}
