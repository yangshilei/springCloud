package com.demo.rocketmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class RocketMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMqApplication.class,args);
    }
}
