package com.demo.spring.test.baseThread.同步容器;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayListDemo implements Runnable{

    // 线程安全集合
    private static List<Integer> list = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void run() {
        list.add(1);
        System.out.println(Thread.currentThread().getName()+"---"+list.size());
    }

    public static void main(String[] args) {
        ArrayListDemo demo = new ArrayListDemo();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for(int i = 0; i<20;i++){

            pool.execute(demo);
        }
        pool.shutdown();

    }
}
