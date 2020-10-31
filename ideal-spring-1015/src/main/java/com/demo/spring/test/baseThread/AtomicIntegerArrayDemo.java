package com.demo.spring.test.baseThread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Description: 无锁且线程安全的数组
 * @Author: yangshilei
 * @Date:
 */
public class AtomicIntegerArrayDemo {

    static AtomicIntegerArray array = new AtomicIntegerArray(10);

    public static class AddThread implements Runnable{
        @Override
        public void run() {
            for (int i =0;i < 10000 ;i++){
                array.getAndIncrement(i%10);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for(int i =0;i<10;i++){
            threads[i] = new Thread(new AddThread());
        }
        for(int i = 0;i<10;i++){
            threads[i].start();
        }
        for(int i = 0;i<10;i++){
            threads[i].join();
        }
        System.out.println(array);
    }

//    public static void main(String[] args) {
//        AtomicIntegerArray array = new AtomicIntegerArray(10);
//        System.out.println(array.length());
//        System.out.println(array);
//
//
//        for(int i =0;i < 10000 ;i++) {
//            array.getAndIncrement(i%10);
//        }
//        System.out.println(array);
//    }

}
