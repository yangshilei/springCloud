package com.demo.spring.design.strategy_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTypeFactory {

    @Autowired
    private UserServiceConfig userServiceConfig;

    public UserService getUserService(Integer type){

        return userServiceConfig.getUserService().get(type);

    }
}
