package com.demo.spring.service.impl;

import com.demo.spring.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Override
    public void getMoney(Integer money) {
        System.out.println(money+200);
    }


}
