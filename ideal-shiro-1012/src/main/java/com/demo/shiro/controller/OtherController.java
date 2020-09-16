package com.demo.shiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟除登陆以外的接口
 */
@RestController
@RequestMapping("/other")
public class OtherController {

    @GetMapping("test")
    String otherTest(){
        return "success";
    }
}
