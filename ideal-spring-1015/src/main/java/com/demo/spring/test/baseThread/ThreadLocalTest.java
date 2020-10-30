package com.demo.spring.test.baseThread;


import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;


public class ThreadLocalTest {

    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();

    public static class ParseDate implements Runnable{

        private int i = 0;

        public ParseDate(int i){
            this.i = i;
        }

        @Override
        public void run() {
            try {
                SimpleDateFormat format = local.get();
                if(null == format){
                    local.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
                SimpleDateFormat format1 = local.get();
                Date parse = format1.parse("2020-10-10 20:22:" + i );
                System.out.println(Thread.currentThread().getName()+"---> "+ i +" : "+parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        ThreadFactory factory = new CustomizableThreadFactory("ThreadName-");
        // 拒绝策略：当任务数量超过线程池线线程以及队列可处理数量以后，多余的任务如何处理；
        RejectedExecutionHandler handler = new CallerRunsPolicy();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), factory, handler);
        try {
            for(int i=0;i<100;i++){
                // 使用线程池，所有任务在这几个线程池的线程内执行；
                poolExecutor.execute(new ParseDate(i));

                // 下面是可能创建约100个线程，线程未复用，造成了服务器资源的浪费；推荐用上面的线程池；
//                new Thread(new ParseDate(i)).start();//
            }
        }finally {
            poolExecutor.shutdown();
        }

    }

}
