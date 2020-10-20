package com.demo.spring.test.stream;

import com.alibaba.fastjson.JSONObject;
import com.demo.spring.dto.User;
import io.swagger.models.auth.In;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LamdaTest1 {
    public static void main(String[] args) {

        //TODO 1.常规集合操作
        List<String> list =
                Arrays.asList("tom","jack","jimi","bike","ali","jps","jjjjj");

        // stream计算集合内元素数量
        long count = list.stream().count();
        System.out.println(count);


        // 对集合进行条件过滤filter 、 数量限制limit 、再转成新的集合collectors.toList；
        List<String> j = list.stream().filter(item -> item.startsWith("j")).limit(4).collect(Collectors.toList());
        System.out.println("第一天"+j);

        //TODO 2.list集合转map集合
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
        System.out.println("第二天"+collect2);

        //TODO 3.stream求和、求最大值、求最小值
        Stream<Integer> integerStream = Stream.of(10, 20, 30, 10, 5);
        Optional<Integer> reduce = integerStream.reduce((a1, a2) -> (a1 + a2));
        System.out.println(reduce.get());
//        Optional<User> reduce1 = users.stream().reduce(((user1, user2) -> {
//            user1.setAge(Integer.parseInt(user1.getAge()) + Integer.parseInt(user2.getAge()) + "");
//            return user1;
//        }));
//        System.out.println("求和："+reduce1.get().getAge());

        // 求最大最小值
        Optional<User> max = users.stream().max((user1, user2) -> Integer.parseInt(user1.getAge()) - Integer.parseInt(user2.getAge()));
        System.out.println("求最大值"+max);
        Optional<User> min = users.stream().min((user1, user2) -> Integer.parseInt(user1.getAge()) - Integer.parseInt(user2.getAge()));
        System.out.println("求最小值"+min);

        // 4.andMatch或者allMatch
        boolean b = users.stream().anyMatch(a -> {
            return Integer.parseInt(a.getAge()) > 33;
        });
        System.out.println(b);

        // TODO 4.andMatch或者allMatch
        boolean b1 = users.stream().allMatch(User -> {
            return Integer.parseInt(User.getAge()) > 10;
        });
        System.out.println(b1);

        // 过滤并转成集合
        List<User> collect3 = users.stream().
                filter(item -> Integer.parseInt(item.getAge()) > 30).
                collect(Collectors.toList());
        System.out.println("过滤"+collect3);

        Map<String, String> collect4 = users.stream()
                .filter(item -> Integer.parseInt(item.getAge()) > 30)
                .collect(Collectors.toMap(User::getUsername, User::getAge));
        System.out.println("过滤list成map集合"+collect4);

        // 排序 sorted
        users.stream().sorted((o1, o2) -> Integer.parseInt(o1.getAge()) - Integer.parseInt(o2.getAge())).forEach(item -> {
            System.out.println("sorted排序"+item);
        });

        // todo 5.分页筛选数据
        List<Integer> listInt = Arrays.asList(10,30,20,50,100,90);
        List<Integer> collect5 = listInt.stream().skip(2).limit(3).collect(Collectors.toList());
        System.out.println("跳过前2个，显示3个"+collect5);

    }
}
