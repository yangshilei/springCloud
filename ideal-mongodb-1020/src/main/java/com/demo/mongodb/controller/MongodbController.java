package com.demo.mongodb.controller;

import com.demo.mongodb.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ES测试模块")
@RestController
@Slf4j
@RequestMapping("/mongodb")
public class MongodbController {

    @ApiOperation(value = "es测试接口", notes = "es测试接口")
    @PostMapping(value = "/test" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result test(){
        log.info("进入测试接口！");
        return Result.ok("模块成功启动");
    }

}
