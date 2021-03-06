package com.demo.websocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.websocket.dto.UserDto;
import com.demo.websocket.websocket.OneToManyWebSocket;
import com.demo.websocket.websocket.OneToOneWebSocket;
import com.demo.websocket.websocket.UserWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("")
public class WebSocketController {

    @Autowired
    private OneToManyWebSocket oneToManyWebSocket;

    /**
     * 模拟当后台数据有变动，调用websocket的一对多方法，对所有的客户端进行数据推送；
     * @return
     */
    @GetMapping(value = "/send/messageToAll/")
    String sendMessage(){
        log.info("模拟后台产生数据推送");
        Map<String,String> map = new HashMap<>();
        // userId为后台websocket推送消息唯一识别码；
        map.put("userId","13");
        map.put("message","hello world ysl");
        oneToManyWebSocket.sendMessage(JSONObject.toJSONString(map),null);
        return "测试消息发送结束";
    }


    @Autowired
    private UserWebSocket userWebSocket;

    /**
     * 模拟给同一个用户的不同客户段发送消息
     * @return
     */
    @PostMapping(value = "/send/user")
    String sendUser(@RequestBody UserDto userDto){
        log.info("开始给用户发送消息");
        userWebSocket.sendToUser(userDto);
        return "成功";
    }




}
