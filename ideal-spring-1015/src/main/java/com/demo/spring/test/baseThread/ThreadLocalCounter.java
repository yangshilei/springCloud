package com.demo.spring.test.baseThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalCounter {

    // ThreadLocal定义的是各个线程中的一个副本值，只针对执行的那个线程，其对象值其它线程是看不到的；
    private ThreadLocal<Integer> num = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public int getNextNum(){
        num.set(num.get()+1);
        return num.get();
    }

    public static void main(String[] args) {
        ThreadLocalCounter threadLocalCounter = new ThreadLocalCounter();
        Counter counter = new Counter(threadLocalCounter);
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(counter);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
    }

    static class Counter implements Runnable{

        private ThreadLocalCounter threadLocalCounter;

        public Counter(ThreadLocalCounter threadLocalCounter){
            this.threadLocalCounter = threadLocalCounter;
        }


        @Override
        public void run() {
            for(int i = 0; i < 20; i++){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("TestThread-"+Thread.currentThread().getName() +": " + threadLocalCounter.getNextNum());
            }
        }
    }

}
