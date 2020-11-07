package com.demo.spring.test.baseThread.并行流水线;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Div implements Runnable {

    public static BlockingQueue<PSMsg> bq = new LinkedBlockingQueue<PSMsg>();;

    @Override
    public void run() {
        while (true){
            try {
                PSMsg msg = bq.take();
                msg.i = msg.i/2;
                System.out.println(msg.orgStr+"="+msg.i);
            }catch (Exception e){
                System.err.println("除法线程异常"+e);            }
        }
    }
}
