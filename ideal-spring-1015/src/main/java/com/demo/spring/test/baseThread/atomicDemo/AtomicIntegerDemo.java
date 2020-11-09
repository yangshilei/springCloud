package com.demo.spring.test.baseThread.atomicDemo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:  AtomicInteger是线程安全类型，采用了CAS算法；
 */
public class AtomicIntegerDemo implements Callable<Integer> {

    private AtomicInteger atomicInteger;

    public AtomicIntegerDemo(AtomicInteger atomicInteger){
        this.atomicInteger = atomicInteger;
    }

    @Override
    public Integer call() throws Exception {
        Integer andIncrement = atomicInteger.getAndIncrement();
        System.out.println(Thread.currentThread().getName()+"==="+andIncrement);
        return andIncrement;
    }


    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicIntegerDemo demo = new AtomicIntegerDemo(atomicInteger);

        Future<?> submit = null;
        for(int i = 0 ;i < 8;i++){
            FutureTask<Integer> task = new FutureTask(demo);
            submit = pool.submit(task);
        }
        pool.shutdown();
    }
}
