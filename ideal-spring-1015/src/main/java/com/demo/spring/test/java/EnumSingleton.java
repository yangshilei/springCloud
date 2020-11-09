package com.demo.spring.test.java;

/**
 * @Description: 线程安全的枚举式单例模式：推荐使用的单例
 * @Author: yangshilei
 */
public class EnumSingleton {

    private EnumSingleton(){
        System.out.println("测试该单例被调用次数");
    }

    public static EnumSingleton getEnumSingleton(){
        return Singleton.INSTANCE.getInstance();
    }

    // 内部枚举类
    private enum Singleton{

        INSTANCE;

        private EnumSingleton enumSingleton;

        // JVM保证该方法绝对只被调用一次
        Singleton(){
            enumSingleton = new EnumSingleton();
        }

        public EnumSingleton getInstance(){
            return enumSingleton;
        }
    }


    public static void main(String[] args) {
        EnumSingleton enumSingleton1 = EnumSingleton.getEnumSingleton();
        System.out.println(1);
        EnumSingleton enumSingleton2 = EnumSingleton.getEnumSingleton();
        System.out.println(2);
        EnumSingleton enumSingleton3 = EnumSingleton.getEnumSingleton();
        System.out.println(3);
    }
}
