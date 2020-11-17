package com.demo.spring.test;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法引入的规范
 * 1.方法名上的参数类型必须和接口函数方法参数类型保持一致
 * 2.
 */
public class Test {

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("a".hashCode(), "hello");
        map.put(97, "hehe");
        System.out.println(JSONObject.toJSONString(map));
        System.out.println("a".hashCode());

    }

}
