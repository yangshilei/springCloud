package com.demo.sftp;

import com.demo.sftp.sftp.SftpFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync // 开启spring异步注解
public class SftpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SftpApplication.class,args);
    }

    @Bean
    public SftpFactory sftpFactory(){
        return new SftpFactory();
    }

}
