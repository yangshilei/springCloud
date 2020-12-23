package com.demo.sftp.controller;

import com.demo.sftp.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api("sftp学习接口")
@RestController
@RequestMapping(value = "/sftp")
public class TestController {

    @ApiOperation(value = "测试接口",notes = "测试接口")
    @PostMapping("/test")
    Result test(){
        log.info("请求正常");
        return Result.ok("success");
    }

}
