package com.demo.spring.design.strategy_model.Impl;

import com.demo.spring.design.strategy_model.UserService;
import org.springframework.stereotype.Service;

@Service
public class JackServiceImpl implements UserService {
    @Override
    public String getUserName(String username) {
        return "我是jack，"+username;
    }
}
