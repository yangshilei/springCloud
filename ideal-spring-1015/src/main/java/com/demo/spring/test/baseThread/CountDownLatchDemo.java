package com.demo.spring.test.baseThread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CountDownLatch:倒计时器
 */
public class CountDownLatchDemo implements Runnable{

    // 初始化倒计数器：10个线程
    static final CountDownLatch latch = new CountDownLatch(10);
    // 使用线程安全的AtomicInteger来输出1-10
    static final AtomicInteger integer = new AtomicInteger();

    @Override
    public void run() {
        // 输出线程的名称和数值：方便查看结果执行情况：
        System.out.println(Thread.currentThread().getName()+":"+integer.incrementAndGet());

        // 模拟业务执行耗时
        try {
            Thread.sleep(new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }


    public static void main(String[] args) {
        // 创建线程池，有4个执行线程
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatchDemo demo = new CountDownLatchDemo();
        for(int i = 0;i<10;i++){
            pool.submit(demo);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 记得关闭线程池
            pool.shutdown();
        }

        System.out.println("打印线程任务完成----");
        System.out.println("主线程任务继续完成执行---");
    }
}
