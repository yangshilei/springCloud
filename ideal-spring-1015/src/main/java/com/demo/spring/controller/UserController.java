package com.demo.spring.controller;

import com.demo.spring.design.strategy_model.UserService;
import com.demo.spring.design.strategy_model.UserTypeFactory;
import com.demo.spring.dto.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api("策略模式学习")
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserTypeFactory factory;

    @ApiOperation(value = "策略模式执行查询用户名",notes = "策略模式执行查询用户名")
    @GetMapping("/test")
    Result getUserName(Integer type,String username){
        UserService userService = factory.getUserService(type);
        String userName = "";
        if(null != userService){
            userName = userService.getUserName(username);
        }
        return Result.ok(userName);
    }

}
