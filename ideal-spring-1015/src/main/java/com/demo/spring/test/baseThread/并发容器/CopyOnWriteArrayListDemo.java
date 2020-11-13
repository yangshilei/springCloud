package com.demo.spring.test.baseThread.并发容器;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: juc包中的线程并发容器
 * @Author: yangshilei
 */
public class CopyOnWriteArrayListDemo implements Runnable{

    private static List<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
    private CountDownLatch countDownLatch;

    public CopyOnWriteArrayListDemo(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        synchronized (this){ // 如果不加锁，下面的输出结果可能会存在重复的值
            copyOnWriteArrayList.add(1);
            System.out.println(Thread.currentThread().getName()+":"+copyOnWriteArrayList.size());
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        CountDownLatch countDownLatch = new CountDownLatch(50);
        CopyOnWriteArrayListDemo demo = new CopyOnWriteArrayListDemo(countDownLatch);
        try {
            for(int i = 0;i<50;i++){
                pool.execute(demo);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("the end "+copyOnWriteArrayList.size());
        }finally {
            pool.shutdown();
        }

    }
}
