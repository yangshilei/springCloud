package com.demo.spring.design.strategy_model;

import com.demo.spring.design.strategy_model.Impl.JackServiceImpl;
import com.demo.spring.design.strategy_model.Impl.TomServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式的关键，动态获取对应的业务实现类；
 */
@Configuration
public class UserServiceConfig {

    @Resource
    private TomServiceImpl tomService;

    @Resource
    private JackServiceImpl jackService;


    @Bean
    public Map<Integer,UserService> getUserService(){
        Map<Integer,UserService> map = new HashMap<>();
        map.put(1,tomService);
        map.put(2,jackService);
        return map;
    }
}
