package com.demo.spring.test.baseThread.atomicDemo;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description:  LongAdder原子性操作演示，以及线程池的活跃和剩余线程展示
 * @Author: yangshilei
 * @Date:
 */
public class LongAdderDemo {

    public static void main(String[] args) {
        int corePoolSize = 4;
        int maxmumPoolSize = 8;
        long keepAliveTime = 0L;
        BlockingQueue blockingQueue = new ArrayBlockingQueue(100);
        ThreadFactory factory = new CustomizableThreadFactory("ThreadName=");
        RejectedExecutionHandler handler = new CallerRunsPolicy();

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize,maxmumPoolSize,keepAliveTime,TimeUnit.SECONDS,
                blockingQueue,factory,handler);

        //设置线程的并发量
        final Semaphore semaphore=new Semaphore(maxmumPoolSize);

        //线程同步类
        final CountDownLatch countDownLatch=new CountDownLatch(maxmumPoolSize);

        LongAdder longAdder = new LongAdder();
        try {
            for(int i  = 0;i<maxmumPoolSize;i++){
                poolExecutor.submit(() -> {
                    try {
                        semaphore.acquire();
                        longAdder.add(1);
                        System.out.println("线程"+Thread.currentThread().getName()+"的值="+longAdder);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("剩余活跃线程数量=="+poolExecutor.getActiveCount());
                    // 当前线程执行完才释放
                    countDownLatch.countDown();
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            poolExecutor.shutdown();
        }
        System.out.println("剩余空闲线程数量=="+(maxmumPoolSize-poolExecutor.getActiveCount()));
        System.out.println("最后="+longAdder);
    }

}
