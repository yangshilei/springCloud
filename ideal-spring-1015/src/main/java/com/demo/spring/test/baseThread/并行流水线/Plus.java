package com.demo.spring.test.baseThread.并行流水线;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @Description: 计算加法的线程
 * @Author: yangshilei
 * @Date:
 */
public class Plus implements Runnable{
    public static BlockingQueue<PSMsg> bq = new LinkedBlockingQueue<PSMsg>();

    @Override
    public void run() {
        while (true){
            try {
                PSMsg msg = bq.take();
                msg.j = msg.i+msg.j;
                Multiply.bq.add(msg);
            }catch (Exception e){
                System.err.println("加法线程异常"+e);
            }
        }
    }
}
