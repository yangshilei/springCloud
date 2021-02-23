package com.demo.spring.controller;

import com.demo.spring.dto.Result;
import com.demo.spring.service.SingService;
import com.demo.spring.service.impl.DogSingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 接口多实现引入方式对比
 */
@Api("接口多实现引入方式对比")
@RestController
@RequestMapping("sing")
@Slf4j
public class SingController {

    // 1. @Autowired + @Qualifier("")组合形式
    @Autowired
    @Qualifier("DogSingServiceImpl")
    private SingService singService;

//    // 2. @Resource 指定具体的实现
//    @Resource(name = "BirdSingServiceImpl")
//    private SingService singService;

    // 3. @Primary 在接口的某个具体实现上添加该注解，@Autowired引入时候，不会报错，默认使用添加了@Primary注解的实现类
//    @Autowired
//    private SingService singService;

    @ApiOperation(value = "唱歌多实现",notes = "唱歌多实现")
    @GetMapping("/sing")
    Result sing(){
        return singService.sing();
    }


}
