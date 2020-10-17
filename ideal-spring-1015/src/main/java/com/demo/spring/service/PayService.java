package com.demo.spring.service;

import com.demo.spring.dto.Result;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @FunctionalInterface：用于检查该接口是否是函数接口
 * 接口中定义的普通非抽象方法：实现类可以继承
 */
@FunctionalInterface
public interface PayService {

    void getMoney(Integer money);

    default String showName(String name){
        return "hello "+name;
    }

    default void getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在时间 "+ format.format(new Date()));
    }



}
