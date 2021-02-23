package com.demo.spring.service.impl;

import com.demo.spring.dto.Result;
import com.demo.spring.service.SingService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("BirdSingServiceImpl")
public class BirdSingServiceImpl implements SingService {
    @Override
    public Result sing() {
        return Result.ok("鸟儿唱歌");
    }
}
