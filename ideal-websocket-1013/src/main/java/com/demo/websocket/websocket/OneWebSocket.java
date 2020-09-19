package com.demo.websocket.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 自己给自己发送消息
 * @Author: yangshilei
 * @Date:
 */

@Slf4j
@Component
@ServerEndpoint(value = "/test/one")
public class OneWebSocket {

    /** 记录当前在线连接数 */
    private static AtomicInteger onlineCount = new AtomicInteger();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session){
        onlineCount.incrementAndGet();// 在线数加1
        log.info("有新连接加入：{}，当前在线人数为：{}",session.getId(),onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session){
        onlineCount.decrementAndGet();// 在线减1
        log.info("有一连接关闭：{}，当前在线人数：{}",session.getId(),onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,Session session){
        log.info("服务端收到客户端[{}]的消息[{}]",session.getId(),message);
        this.sendMessage("hello," + message,session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String message,Session session){
        try {
            log.info("服务端给客户端【{}】发送消息【{}】",session.getId(),message);
            session.getBasicRemote().sendText(message);
        }catch (Exception e){
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }

}
