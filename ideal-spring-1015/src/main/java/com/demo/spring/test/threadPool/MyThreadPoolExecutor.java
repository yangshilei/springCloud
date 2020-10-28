package com.demo.spring.test.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 简单模拟手写线程池，方便理解线程池工作原理
 * @Author: 狗狗大蛇
 * @Date:
 * 线程池基本的构成要素
 * 1.执行的线程数量，开启执行的线程；
 * 2.执行的任务集合
 * 3.构造方法
 * 4.executor方法或者
 */
public class MyThreadPoolExecutor {

    // 工作线程集合
    private List<TaskThread> threads;

    // 阻塞队列：executor（Runnable 实参）传进来的工作任务均先存放到该队列中，等待WorkThread去取出来执行；
    private BlockingQueue<Runnable> workQueue;

    // 控制线程池停止的标记
    private volatile Boolean shutDownFlag = true;

    /**
     * 线程池构造方法
     * @param threadNum : 初始创建保持运行状态线程的数量
     * @param works : 线程池处理的工作任务数量
     */
    public MyThreadPoolExecutor(int threadNum,int works){

        // 创建指定大小的队列容器，用于存放任务；
        this.workQueue = new LinkedBlockingQueue<>(works);

        // 创建threadNum运行状态的线程数量,并进入就绪状态
        this.threads = new ArrayList<>();
        for(int i = 0 ; i < threadNum; i++){
            TaskThread workThread = new TaskThread();
            Thread thread = new Thread(workThread);
            thread.start();
//            this.threads.add(workThread);
        }


    }

    /**
     * 定义一个工作线程:
     * 1.在线程池中始终保持运行状态；
     * 2.不停的去工作任务队列中去取任务；
     */
    class TaskThread implements Runnable{
        @Override
        public void run() {
            // 1.如果要线程保持运行状态，需要写死循环
            while (shutDownFlag){
                // 2.队列中取任务执行，取完删掉对应任务；
                Runnable task = null;
                try {
                    // 此处不用poll，poll会不停的取，导致CPU使用率居高不下；用take会等待;
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(null != task){
                    task.run();
                }
            }
        }
    }

    /**
     * 线程池启动执行的方法
     * @param runnable 放入需要执行的业务任务线程；
     * @return
     */
    public void executor(Runnable runnable){
        // 将业务线程放到队列容器中，运行中的线程会不停的到队列中取任务；
        // offer(): 非阻塞式添加任务
        try {
            workQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.shutDownFlag = false;
    }

    // 测试效果
    public static void main(String[] args) throws InterruptedException {
        MyThreadPoolExecutor myThreadPoolExecutor = new MyThreadPoolExecutor(4,8);
        SendEmailTask sendEmailTask = new SendEmailTask();
        boolean flag = false;
        for(int i = 0; i < 100; i++){
            myThreadPoolExecutor.executor(sendEmailTask);
            if( i == 99 ){
                flag = true;
            }
        }

        Thread.sleep(10000);
        System.out.println("模拟等待10秒");
        myThreadPoolExecutor.executor(sendEmailTask);
        System.out.println("结束");

        Thread.sleep(10000);
//        if(flag){
//            myThreadPoolExecutor.shutdown();
//        }

    }


}

/**
 * @Description: 具体发送邮件的业务线程
 * @Author: yangshilei
 * @Date:
 */
class SendEmailTask implements Runnable{

    @Override
    public void run() {
        try {
            // 模拟实际业务处理占用的时间
            Thread.sleep(new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程:"+Thread.currentThread().getName() + ":" + "短信发送成功");
    }

}
