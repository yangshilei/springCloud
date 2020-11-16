package com.demo.spring.test.baseThread.并发容器;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:  并发容器
 * @Author: yangshilei
 */
public class ConcurrentHashMapDemo implements Runnable{

    private final static ConcurrentHashMap<String,String> map = new ConcurrentHashMap();

    private final static ReentrantLock lock = new ReentrantLock();

    private String key;

    private String value;

    public ConcurrentHashMapDemo(String key,String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            map.put(key,value);
            System.out.println("map集合的大小：" + map.size());
        }finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            for(int i = 1;i<=100;i++){
                ConcurrentHashMapDemo demo = new ConcurrentHashMapDemo("key"+i,"value"+i);
                pool.execute(demo);
            }
        }finally {
            pool.shutdown();
        }

    }
}
