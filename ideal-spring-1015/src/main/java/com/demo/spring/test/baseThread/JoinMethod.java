package com.demo.spring.test.baseThread;

/**
 * @Description: join实现线程互相等待 ，如果A线程掉用B线程，如果B线程没执行完，A现在会一直等待B线程执行完；
 * @Author: yangshilei
 * @Date:
 */
public class JoinMethod implements Runnable{

    private Thread thread;// 该全局变量用于控制线程间的等待关系

    public JoinMethod(Thread thread){
        this.thread = thread;
    }

    @Override
    public void run() {
        if(null != thread){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("线程名称:"+Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(new JoinMethod(null),"窗口A");
        Thread b = new Thread(new JoinMethod(a),"窗口B");
        Thread c = new Thread(new JoinMethod(b),"窗口C");
        a.start();
//        a.join();
        b.start();
//        b.join();
        c.start();
//        c.join();

    }
}
