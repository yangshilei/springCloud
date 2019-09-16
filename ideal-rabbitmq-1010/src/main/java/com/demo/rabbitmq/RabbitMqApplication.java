package com.demo.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description：
 *
 * @Author yangshilei
 * @Date 2019-01-09 15:06
 */
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaClient
public class RabbitMqApplication {
  public static void main(String[] args) {
    SpringApplication.run(RabbitMqApplication.class,args);
  }
}
