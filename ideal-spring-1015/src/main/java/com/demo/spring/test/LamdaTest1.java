package com.demo.spring.test;

import com.alibaba.fastjson.JSONObject;
import com.demo.spring.dto.User;

import java.util.*;
import java.util.stream.Collectors;

public class LamdaTest1 {
    public static void main(String[] args) {

        // 1.常规集合操作
        List<String> list =
                Arrays.asList("tom","jack","jimi","bike","ali","jps","jjjjj");

        // stream计算集合内元素数量
        long count = list.stream().count();
        System.out.println(count);


        // 对集合进行条件过滤filter 、 数量限制limit 、再转成新的集合collectors.toList；
        List<String> j = list.stream().filter(item -> item.startsWith("j")).limit(4).collect(Collectors.toList());
        System.out.println(j);

        // 2.list集合转map集合
        List<User> users = new ArrayList<>();
        users.add(new User("tom","20"));
        users.add(new User("jack","23"));
        users.add(new User("bili","32"));
        users.add(new User("tom","20"));
        users.add(new User("kaili","33"));
        Set<User> collect = users.stream().collect(Collectors.toSet());
        System.out.println(collect);

        // 根据实例对象获取对应属性
        Map<String, String> collect1 = collect.stream().collect(Collectors.toMap(user -> user.getUsername(), user -> user.getAge()));
        System.out.println(collect1);
        // 根据类直接指定方法
        Map<String, String> collect2 = collect.stream().collect(Collectors.toMap(User::getUsername, User::getAge));
        System.out.println(collect2);


    }
}
