package com.demo.websocket.websocket;

import com.alibaba.fastjson.JSON;
import com.demo.websocket.config.WebSocketConfig;
import com.demo.websocket.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@ServerEndpoint(value = "/show/user/peoples")
public class UserWebSocket {

    private static Map<String, List<Session>> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("有新的客户端上线: {}", session.getId());
        List<Session> list = new CopyOnWriteArrayList<>();
        list.add(session);
        clients.put(session.getId(), list);
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        log.info("有客户端离线: {}", sessionId);
        clients.remove(sessionId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        if (clients.get(session.getId()) != null) {
            clients.remove(session.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session){
        UserDto userDto = JSON.parseObject(message, UserDto.class);
        List<Session> list = new CopyOnWriteArrayList<>();
        list.add(session);
        clients.put(userDto.getUserId(),list);
        log.info("用户新的客户端加入成功");
    }

    /**
     * 发送消息
     *
     * @param userDto 消息对象
     */
    public void sendToUser(UserDto userDto) {
        List<Session> list = clients.get(userDto.getUserId());
        if(null != list){
            log.info("需要发送的消息个数==={}",list.size());
            for(Session  session : list){
                try {
                    session.getBasicRemote().sendText(userDto.toString());
                }catch (IOException e){
                    log.error("发送websocket消息失败：{}", e);
                }

            }
        }
    }


}
