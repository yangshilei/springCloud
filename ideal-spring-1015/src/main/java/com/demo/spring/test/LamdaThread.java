package com.demo.spring.test;

/**
 * @Description: lamda创建线程
 * @Author: yangshilei
 * @Date:
 */
public class LamdaThread {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始主线程");
        new Thread(()->{
            System.out.println("我是一个子线程"+Thread.currentThread().getName());
        }).start();

        System.out.println("主线程即将停止3秒");
        Thread.currentThread().sleep(3000);
        System.out.println("主线程又开始输出");
    }
}
