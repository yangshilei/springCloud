package com.demo.spring.test.java;

import java.lang.reflect.Constructor;

/**
 * @Description: 单例模式：
 */
public class Singleton {

    // 私有构造方法
    private Singleton(){
        // 用来记录感知是否进行实例化
        System.out.println("创建一个懒加载模式且线程安全的单例");
    }

    private static class SingletonInst{
        private static Singleton singleton = new Singleton();
    }

    public static Singleton getSingleton(){

        return SingletonInst.singleton;
    }


    public static void main(String[] args) {
        // 第一次获取实例
        Singleton singleton1 = Singleton.getSingleton();
        System.out.println("结束1");

        // 第二次获取实例
        Singleton singleton2 = Singleton.getSingleton();
        System.out.println("结束2");

        // 第三次获取实例
        Singleton singleton3 = Singleton.getSingleton();
        System.out.println("结束3");

    }
}
