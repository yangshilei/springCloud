package com.demo.spring.test.baseThread.同步容器;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 线程安全，但是并发环境并不推荐使用，会出错
 * @Author: yangshilei
 */
public class VectorDemo implements Runnable{

    private static Vector vector = new Vector();

    @Override
    public void run() {
        vector.add(1);
        System.out.println(Thread.currentThread().getName()+":"+vector.size());
    }

    public static void main(String[] args) {
        VectorDemo demo = new VectorDemo();
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            for(int i = 0; i<20;i++){
                pool.execute(demo);
            }
        }finally {
            pool.shutdown();
        }
    }
}
