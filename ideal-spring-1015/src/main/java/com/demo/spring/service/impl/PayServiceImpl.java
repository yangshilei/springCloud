package com.demo.spring.service.impl;

import com.demo.spring.dto.Result;
import com.demo.spring.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Override
    public void getMoney(Integer money) {
        System.out.println(money+200);
    }

}
