package com.demo.websocket.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.websocket.websocket.OneToManyWebSocket;
import com.demo.websocket.websocket.OneToOneWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        map.put("userId","1");
        map.put("message","hello world");
        oneToManyWebSocket.onMessage(JSONObject.toJSONString(map),null);
        return "测试消息发送结束";
    }





}
