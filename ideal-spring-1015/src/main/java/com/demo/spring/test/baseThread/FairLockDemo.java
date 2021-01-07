package com.demo.spring.test.baseThread;

import java.util.concurrent.locks.ReentrantLock;

/** 
 * @Description: 重入锁：公平和非公平锁
 * @Author: yangshilei
 */
public class FairLockDemo implements Runnable{

    // 公平锁，按照顺序去执行
    public static ReentrantLock lock = new ReentrantLock(true);

    @Override
    public void run() {
        while (true){
            try {
                lock.lock();
                lock.lock();
                Thread.sleep(1000);
                System.out.println("线程名称："+Thread.currentThread().getName());
            }catch (Exception e){
                System.out.println("异常");
            }
            finally {
                lock.unlock();
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FairLockDemo demo = new FairLockDemo();

        Thread thread1 = new Thread(demo,"线程1");
        Thread thread2 = new Thread(demo,"线程2");
        Thread thread3 = new Thread(demo,"线程3");
        Thread thread4 = new Thread(demo,"线程4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
