package com.demo.spring.test.baseThread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 设计使用synchronized线程安全来调用4个线程输出1-100
 * @Author: yangshilei
 * @Date:
 */
public class SynCounter implements Runnable{

    int num = 1;

    // 无锁且线程安全的整数，可以替换用synchronized；
    AtomicInteger integer = new AtomicInteger();

    @Override
    public void run() {
        int i = integer.incrementAndGet();
        System.out.println(Thread.currentThread().getName()+":"+i);
//        synchronized (this){
//            if(num <= 100){
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName()+":"+num);
//            }
//            num++;
//        }
    }


    public static void main(String[] args) {
        SynCounter synCounter = new SynCounter();
        int corePoolSize = 4; // 线程池中运行的线程数量
        int maximumPoolSize = 8; // 线程池中最大线程数量
        long keepAliveTime = 0L; // 空闲线程的存活时间
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100); // 任务队列，被提交但是尚未执行的任务

        ThreadFactory threadFactorynew = new CustomizableThreadFactory("ThreadName-");//线程工厂，一般用默认的就行；
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("order-net-%d").build(); // build创建线程工厂模式；


        RejectedExecutionHandler handler = new CallerRunsPolicy(); // 拒绝策略：当任务数量超过线程池线线程以及队列可处理数量以后，多余的任务如何处理；
        // 自定义线程池
        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        workQueue, threadFactorynew,handler);

        // 创建的线程池：阿里规范不推荐，因为默认使用了LinkedBlockingQueue无界模式，容易导致资源耗尽；
//        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i = 0; i< 100;i++){
            poolExecutor.submit(synCounter);
        }

        poolExecutor.shutdown();
    }
}
