package com.demo.spring.test.baseThread;

/**
 * 如何优雅的停掉线程
 * 尽量通过某个条件去控制线程的结束
 * 不能直接使用thread.stop方法，该方法后期将淘汰；
 */
public class StopThread implements Runnable{

    private volatile Boolean flag = true;

    @Override
    public void run() {
        Integer i = 0;
        while(flag){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i++);
        }
    }

    private void stopThread(){
        flag = false;
    }

    public static void main(String[] args) throws InterruptedException {
        StopThread stopThread = new StopThread();
        Thread thread = new Thread(stopThread);
        thread.start();

        Thread.sleep(3000);
        System.out.println("即将停止线程");
        stopThread.stopThread();
//        thread.stop();
    }
}
