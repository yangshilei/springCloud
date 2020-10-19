package com.demo.spring.test;


/**
 * 模拟多线程产生死锁
 * 模拟过程：在object同步方法锁中使用嵌套同步this锁；多线程同时调用，产生死锁
 *
 * 查看方式：进入java安装bin目录下，打开jconsole.exe，选择对应的线程执行方法，然后选择线程标签，再检测死锁；
 */
public class DeadLock implements Runnable {

    private static int count = 100;
    private Object object = new Object();
    private boolean flag = true;

    @Override
    public void run() {
        if(flag){
            while (count > 0){
                // 此处使用object锁,内部嵌套this锁
                synchronized (object){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.ticket();
                }
            }
        }else {
            while (count > 0){
                this.ticket();
            }
        }

    }

    // 本方法使用this锁，内部嵌套object锁
    private synchronized void ticket(){
        synchronized (object){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count > 0){
                System.out.println("方法："+Thread.currentThread().getName()+":"+count);
                count--;
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        DeadLock deadLock = new DeadLock();
        Thread thread1 = new Thread(deadLock,"窗口A");
        Thread thread2 = new Thread(deadLock,"窗口B");
        Thread thread3 = new Thread(deadLock,"窗口C");
        thread1.start();
        Thread.sleep(100);
        deadLock.flag = false;
        thread2.start();
        thread3.start();
    }
}
