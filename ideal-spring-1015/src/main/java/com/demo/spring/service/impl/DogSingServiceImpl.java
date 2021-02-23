package com.demo.spring.service.impl;

import com.demo.spring.dto.Result;
import com.demo.spring.service.SingService;
import org.springframework.stereotype.Service;

@Service("DogSingServiceImpl")
public class DogSingServiceImpl implements SingService {

    @Override
    public Result sing() {
        return Result.ok("小狗唱歌");
    }
}
