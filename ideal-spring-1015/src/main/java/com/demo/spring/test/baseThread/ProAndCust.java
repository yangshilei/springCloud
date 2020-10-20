package com.demo.spring.test.baseThread;



/**
 * @Description: 模拟生产者和消费者，相互等待通知等情况
 * 模拟输出 Jack:20； Tom:55;
 * @Author: yangshilei
 * @Date:
 */
public class ProAndCust {

    class User{
        public Boolean flag = false;// 作为线程通信的判断条件:false:需写数据，当为true：写好可以读数据
        public String username;
        private String age;
    }

    class WriteThread implements Runnable {

        User user;

        public WriteThread(User user){
            this.user = user;
        }

        // 定义一个局部变量num;
        // 当num=0时候，输出Jack；
        @Override
        public void run() {
            int num = 0;
            while (true){
                // 同步方法中加锁才有作用
                synchronized (user){
                    System.out.println("写线程的标记flag={}"+user.flag);
                    if(user.flag){ // user是true的时候不能写，能读；
                        try {
                            user.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(num == 0){
                        user.username = "Jack";
                        user.age = "20";
                    }else {
                        user.username = "Tom";
                        user.age = "55";
                    }
                    System.out.println("用户写入完成");

                    // 如果线程不堵塞，写完数据后，要将标记修改为已写完，flag = true,并通知消费者，释放当前线程锁；
                    user.flag = true;
                    user.notify();
                    // 负载均衡器:取模2；
                    num = (num + 1) % 2;
                }
            }
        }
    }

    class ReadThread implements Runnable{

        User user;

        public ReadThread(User user){
            this.user = user;
        }

        @Override
        public void run() {
            while (true){
                synchronized (user){
                    if(!user.flag){ // flag是true的时候才能读取，不能写；
                        try {
                            user.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("用户读取："+user.username+":"+user.age);

                    // 读完以后需要重新写
                    user.flag = false;
                    user.notify();

                }
            }
        }
    }

    public static void main(String[] args) {
        new ProAndCust().beginStart();
    }

    public void beginStart(){
        User user = new User();
        // 写的线程
        new Thread(new WriteThread(user)).start();
        // 读的线程
        new Thread(new ReadThread(user)).start();
    }
}
