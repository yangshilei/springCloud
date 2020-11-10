package com.demo.spring.test.java;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 不可变对象 final修饰符
 * @Author: yangshilei
 */
public class FinalDemo {

    private static final Map<Integer,String> map = new HashMap<>();

    private static final String ss = "hello";

    private static final ImmutableList<Integer> immuTableList = ImmutableList.of(1,2,4);

    static {
        map.put(1,"aa");
        map.put(2,"bb");
        map.put(3,"cc");
    }
    
    public static void main(String[] args) {
        System.out.println(map.get(3));
        map.put(1,"fff");
        System.out.println(map.get(1));
        map.put(4,"hhh");
        System.out.println(map.get(4));


        System.out.println(immuTableList.toString());
    }



}
