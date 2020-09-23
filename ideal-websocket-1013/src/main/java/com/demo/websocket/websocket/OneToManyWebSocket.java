package com.demo.websocket.websocket;

import com.demo.websocket.config.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发送给所有客户端
 */
@Slf4j
@Component
@ServerEndpoint(value = "/test/oneToMany/{userId}", configurator = WebSocketConfig.class)
public class OneToManyWebSocket {

    private static AtomicInteger onlineCount  = new AtomicInteger(0);

    private static Map<String, Session> clients = new HashMap<>();


    /**
     * 连接建立成功调用的方法
     * 如果需要给指定用户发送消息，必须加上@PathParam("userId")
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session){
        onlineCount.incrementAndGet();
        clients.put(userId,session);
        log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session){
        onlineCount.decrementAndGet();
        clients.remove(session.getId());
        log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法, 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        log.info("服务端收到客户端[{}]的消息:{}", session.getId(), message);
        this.sendMessage(message, session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 模拟给指定或者所有用户发送消息
     */
    public void sendMessage(String message,Session session){
        log.info("发送的消息==={}",message);

        // 1.此处模拟发送消息给userId=13的用户
        Session session13 = clients.get("13");
        if(null != session13){
            try {
                session13.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. 下面为模拟给所有客户端发送消息
//        for(Map.Entry<String,Session> sessionEntry: clients.entrySet()){
//            Session value = sessionEntry.getValue();
//            // 排除掉自己
//            try {
//                value.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


    }


}
