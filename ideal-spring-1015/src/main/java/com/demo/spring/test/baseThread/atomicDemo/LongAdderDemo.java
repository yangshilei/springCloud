package com.demo.spring.test.baseThread.atomicDemo;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.atomic.LongAdder;

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

        LongAdder longAdder = new LongAdder();
        try {
            for(int i  = 0;i<100;i++){
                poolExecutor.submit(() -> {
                    longAdder.add(1);
                    System.out.println("执行任务="+longAdder);
                });
            }

        }finally {
            poolExecutor.shutdown();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===="+longAdder);
    }

}
