package com.demo.spring.test.baseThread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestReadWriteLock {

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // 读锁：读读可以共享锁，类似没有上锁
    private void read(){
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+":开始读取数据");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":读取结束");
        }finally {
            readWriteLock.readLock().unlock();
        }
    }


    // 写锁：写写不能共享锁，保证线程安全，只能一个线程执行写；
    private void write(){
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+":开始写入数据");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":写入完成");
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }


    public static void main(String[] args) {
        TestReadWriteLock testReadWriteLock = new TestReadWriteLock();
        // 测试读锁线程
        for(int i = 1 ;i<=8;i++){
            new Thread(()->{
                testReadWriteLock.read();
            },"读线程"+i).start();
        }

        // 测试写入线程
        for(int i = 1 ;i<=8;i++){
            new Thread(()->{
                testReadWriteLock.write();
            },"写线程"+i).start();
        }
    }
}
