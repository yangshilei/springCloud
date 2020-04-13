package com.demo.syn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author ：yangshilei
 * @Date ：2020/4/13 17:25
 * @Description：
 */
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
public class SynApplication {
    public static void main(String[] args){
        SpringApplication.run(SynApplication.class,args);
    }
}
