package com.demo.spring.test;

import com.demo.spring.service.PayService;
import com.demo.spring.service.impl.PayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 方法引入的规范
 * 1.方法名上的参数类型必须和接口函数方法参数类型保持一致
 * 2.
 */
public class Test {

    @Autowired
    private PayServiceImpl payService;

    public static void main(String[] args) {

        // 第一种：直接定义大括号中方法实现
        PayService payService1 = (money) -> {
            if(money > 100){
                System.out.println(money+33);
            }else {
                System.out.println(money+55);
            }
        };

        // 调用上面的实现接口的方法
        payService1.getMoney(300);

        // 第二种：方法实现未下面的方式
        PayService payService2 = Test::getMoney;
        payService2.getMoney(500);

//        PayService payService3 = PayServiceImpl::getMoney;
    }


    public static void getMoney(Integer money){
        System.out.println(money+55);
    }
}
