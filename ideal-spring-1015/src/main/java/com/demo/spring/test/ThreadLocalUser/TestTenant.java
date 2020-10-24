package com.demo.spring.test.ThreadLocalUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTenant {
    public static void main(String[] args) {
        HtmlRequest htmlRequest = new HtmlRequest();

        // 1.新创建线程，线程id不会出现重复，都是新创建的
        for(int i = 1; i <= 100; i++){
            new Thread(htmlRequest).start();
        }


        // 2.线程池的方式：重复使用线程，线程id会出现重复
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        for(int i = 1; i <= 100; i++){
//            executorService.submit(htmlRequest);
//        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        executorService.shutdown();
    }
}
