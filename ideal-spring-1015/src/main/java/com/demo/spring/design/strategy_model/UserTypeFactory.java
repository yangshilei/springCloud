package com.demo.spring.design.strategy_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 工厂类：可以避免在controller中逐个添加业务实现类
 * @Author: yangshilei
 */
@Component
public class UserTypeFactory {

    @Autowired
    private UserServiceConfig userServiceConfig;

    public UserService getUserService(Integer type){

        return userServiceConfig.getUserService().get(type);

    }
}
