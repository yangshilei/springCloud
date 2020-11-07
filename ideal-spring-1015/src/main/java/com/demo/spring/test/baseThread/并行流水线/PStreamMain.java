package com.demo.spring.test.baseThread.并行流水线;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class PStreamMain {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Plus());
        Thread t2 = new Thread(new Multiply());
        Thread t3 = new Thread(new Div());
        t1.start();
        t2.start();
        t3.start();

        for(int i = 0;i<10;i++){
            for(int j = 0 ;j <5;j++){
                PSMsg msg = new PSMsg();
                msg.i = i;
                msg.j = j;
                msg.orgStr = "("+i+"+"+j+"）*"+i+")/2";
                Plus.bq.add(msg);
            }
        }

    }
}
