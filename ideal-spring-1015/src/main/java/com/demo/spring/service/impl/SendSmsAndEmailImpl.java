package com.demo.spring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Asyn开启spirng异步任务注解使用
 * 1.失效之谜：如果想@Async生效，必须单独新定义一个类来实现，不能再调用的业务类里直接使用，直接使用无效，不会开启异步线程。
 * 2.需要SpringBootApplication启动类当中添加注解@EnableAsync注解。
 * 3.使用注解@Async的返回值只能为void或者Future
 */
@Component
@Slf4j
public class SendSmsAndEmailImpl {

    @Async
    public void sendSmsAndEmail(){
        this.sendSms();
        this.sendEmail();
    }

    private void sendSms(){
        try {
            Thread.sleep(2000);
            log.info("短信发送结束，线程=={}",Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(){
        try {
            Thread.sleep(2000);
            log.info("邮件发送结束，线程=={}",Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
