package com.demo.spring.service.impl;

import com.demo.spring.dto.Result;
import com.demo.spring.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SendSmsAndEmailImpl sendSmsAndEmail;

    /**
     * 测试@Async异步注解功能
     * @return
     */
    @Override
    public Result testAsync() {
        log.info("进入测试async方法中");
        this.queryUser();
        log.info("用户查询成功");
        sendSmsAndEmail.sendSmsAndEmail();
        return Result.ok("登陆成功");
    }

    private void queryUser(){
        try {
            Thread.sleep(2000);
            log.info("查询用户结束，线程=={}",Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
