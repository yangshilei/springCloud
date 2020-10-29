package com.demo.spring.test.threadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description: LinkedBlockingQueue的特点是先进先出的队列容器，默认是属于无界队列；
 * @Author: yangshilei
 * @Date:
 */
public class LinkedBlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue linkedBlockingQueue  = new LinkedBlockingQueue(4);
        linkedBlockingQueue.offer("yangshilei");
        linkedBlockingQueue.offer("tom");
        linkedBlockingQueue.offer("tom2");
        boolean tom3 = linkedBlockingQueue.offer("tom3");
        System.out.println(tom3);

//        try {
//            linkedBlockingQueue.put("tom5");
//        } catch (InterruptedException e) {
//            System.out.println("linkedBlockingQueue队列已经存满，阻塞不能再此添加");
//            e.printStackTrace();
//        }

        // poll取出并删除队列中的消息
        Object poll1 = linkedBlockingQueue.poll();
        System.out.println("poll："+poll1.toString());

        linkedBlockingQueue.put("tom4");
        System.out.println();
        // peek取出不会删除队列中的消息
        Object peek = linkedBlockingQueue.peek();
        System.out.println("peek："+peek.toString());

        for(int i = 1 ;i <= 5;i++){
            Object poll = null;
            try {
//                poll = linkedBlockingQueue.poll(2, TimeUnit.SECONDS);
                poll = linkedBlockingQueue.take();
            } catch (Exception e) {
                System.out.println("取值异常"+i);

            }
            System.out.println(i+":"+poll.toString());
        }

    }


}
