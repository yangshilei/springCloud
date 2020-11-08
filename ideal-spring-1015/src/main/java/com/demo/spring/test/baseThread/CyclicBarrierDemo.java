package com.demo.spring.test.baseThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:  循环栅栏多线程同步工具类
 */
public class CyclicBarrierDemo implements Runnable{

    CyclicBarrier cyclicBarrier;

    public CyclicBarrierDemo(CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName()+"开始执行");
            System.out.println("模拟异步发送短信邮件，发送完成");

            // 任务中需要等待其它线程全部执行到这个位置等待
            cyclicBarrier.await();

            System.out.println(Thread.currentThread().getName()+"等待结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        // 我们设置有5个线程的循环栅栏
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            // 为了体现循环栅栏可循环使用，我们用10个循环线程；
            // 如果是可循环使用的，当有5个线程第一次使用完以后，程序会继续完成执行；
            // 同时，另外5个线程也会因为再此获取到CyclicBarrier实例而继续往下执行；
            for(int i = 0; i < 10; i++){
                CyclicBarrierDemo demo = new CyclicBarrierDemo(cyclicBarrier);
                pool.execute(demo);
            }
        }finally {
            pool.shutdown();
        }

        System.out.println("5个任务执行完成，继续往下进行业务处理");
    }
}
