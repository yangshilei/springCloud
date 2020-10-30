package com.demo.spring.test.baseThread;

import java.util.Random;
import java.util.concurrent.*;

public class ThreadLocalRandom {

    private static final int GEN_COUNT = 10000000;
    private static final int THREAD_COUNT = 4;
    private static ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Random random = new Random(123);

    private static ThreadLocal<Random> randomThreadLocal = new ThreadLocal<Random>(){

        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };


    public static class TestTask implements Callable {

        private int mode = 0;

        public TestTask(int mode){
            this.mode = mode;
        }

        public Random getRandomProd(){
            switch (mode){
                case 0:
                    return random;
                case 1:
                    return randomThreadLocal.get();
                default:
                    return null;
            }

        }

        @Override
        public Object call() throws Exception {
            long begin = System.currentTimeMillis();
            for(int i = 0;i < GEN_COUNT; i++){
                int i1 = this.getRandomProd().nextInt();
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+"花费时间："+(end - begin)+"ms");
            return end-begin;
        }
    }


    public static void main(String[] args) throws Exception {
        Future<Long>[] future = new Future[THREAD_COUNT];

        for(int i = 0; i < THREAD_COUNT; i++){
            future[i] = pool.submit(new TestTask(0));
        }
        Long total = 0L;
        for(int i =0 ;i < THREAD_COUNT; i++){
            total = total + future[i].get();
        }
        System.out.println("4个线程访问同一个Random共计耗时："+total);


        for(int i =0 ;i < THREAD_COUNT; i++){
            future[i] = pool.submit(new TestTask(1));
        }
        total = 0L;
        for(int i =0 ;i < THREAD_COUNT; i++){
            total = total + future[i].get();
        }
        System.out.println("4个线程访问各自线程内的局部Random共计耗时："+total);

        pool.shutdown();

    }
}
