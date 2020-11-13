package com.demo.spring.test.baseThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo implements Runnable{


    final ReentrantLock lock = new ReentrantLock();

    final Semaphore semaphore = new Semaphore(4);

    int count = 0;

    @Override
    public void run() {

        try {
            semaphore.acquire();
            lock.lock();
            try {
                count++;
                System.out.println(Thread.currentThread().getName()+":"+count);
            }finally {
                lock.unlock();
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        synchronized (this){
//            count++;
//            System.out.println(Thread.currentThread().getName()+":"+count);
//        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        ReentrantLockDemo demo = new ReentrantLockDemo();
        try {
            for(int i =0;i<100;i++){
                pool.execute(demo);
            }
        }finally {
            pool.shutdown();
        }

    }
}
