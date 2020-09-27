package com.demo.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author ：yangshilei
 * @Date ：2020/6/9 10:41
 * @Description：控制台输出 Swagger 接口文档地址
 */
@Slf4j
@Component
public class SwaggerAddressConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    public int getPort(){
        return this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            this.serverPort = webServerInitializedEvent.getWebServer().getPort();
            log.info("启动完成，接口文档地址：http://"+localHost.getHostAddress()+":"+serverPort+"/swagger-ui.html");
        } catch (UnknownHostException e) {
            log.info("获取本机的ip异常错误=={}",e);
        }

    }
}
