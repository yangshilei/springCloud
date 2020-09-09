package com.demo.shiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableDiscoveryClient // 除了eureka，还可以注册到其它的注册中心，如zookeeper上；
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableEurekaClient // 只能注册到eureka的注册中心
public class ShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class,args);
    }
}
