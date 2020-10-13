package com.demo.spring.test;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LamdaTest1 {
    public static void main(String[] args) {
        List<String> list =
                Arrays.asList("tom","jack","jimi","bike","ali","jps","jjjjj");

        // stream计算集合内元素数量
        long count = list.stream().count();
        System.out.println(count);


        // 对集合进行条件过滤filter 、 数量限制limit 、再转成新的集合collectors.toList；
        List<String> j = list.stream().filter(item -> item.startsWith("j")).limit(4).collect(Collectors.toList());
        System.out.println(j);
    }
}
