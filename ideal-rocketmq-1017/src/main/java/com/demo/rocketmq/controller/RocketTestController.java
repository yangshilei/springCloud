package com.demo.rocketmq.controller;

import com.demo.rocketmq.dto.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rocketmq")
public class RocketTestController {

    @ApiOperation(value = "rocketmq测试", notes = "rocketmq测试")
    @PostMapping("test")
    Result testRocketmq(){
        return Result.ok("成功");
    }
}
