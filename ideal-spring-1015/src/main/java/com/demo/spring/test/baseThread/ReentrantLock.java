package com.demo.spring.test.baseThread;

/**
 * 可重入锁：基本锁都有可重入性，否则很容易导致死锁
 * 死锁产生原因：在同步方法中嵌套同步方法
 */
public class ReentrantLock implements Runnable{

    private volatile Integer num = 0;

    @Override
    public void run() {
        while (num < 100){
            this.methodA();
        }
    }

    // methodA和methodB可以共用this锁
    private synchronized void methodA(){
        // 此处需要再次校验num，否则如果按照多线程执行，会导致起初等待中的线程在拿到锁后继续执行，num值会等于100;
        if(num < 100){
            System.out.println("我是方法A，开始执行，即将调用方法B，num="+num+":"+Thread.currentThread().getName());
            this.methodB();
        }
    }

    // 也是使用this锁
    private synchronized void methodB(){
        num++;
        System.out.println("我是方法B，模拟发送邮件");
    }

    public static void main(String[] args) {
        ReentrantLock reentryLock= new ReentrantLock();
        Thread thread = new Thread(reentryLock,"窗口1");
        Thread thread1 = new Thread(reentryLock,"窗口2");
        Thread thread2 = new Thread(reentryLock,"窗口3");
        thread.start();
        thread1.start();
        thread2.start();
    }

}
