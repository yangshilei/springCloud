package com.demo.spring.test.baseThread;

import java.util.concurrent.*;

public class FutureDemo implements Callable {

    private String para;

    public FutureDemo(String para){
        this.para = para;
    }

    @Override
    public Object call() throws Exception {
        StringBuffer stringBuffer = new StringBuffer();// StringBuffer是线程安全的；
        for(int i = 0; i<10; i++){
            stringBuffer.append(para);
            Thread.sleep(1000);
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        FutureTask<String> futureTask = new FutureTask<String>(new FutureDemo("hellow "));
        pool.submit(futureTask);
        System.out.println("请求发送完毕");

        try {
            Thread.sleep(2000);
            System.out.println("处理其他业务类型");
        }catch (InterruptedException e){
            System.err.println("出现异常");
        }



        System.out.println("最终数据==="+futureTask.get());
        pool.shutdown();
    }

}

