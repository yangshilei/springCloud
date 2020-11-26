package com.demo.spring.test.java;

import java.lang.reflect.Constructor;

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


        // 使用反射破坏单例测试代码
        try {
            System.out.println("开始s反射破解单例");
            Class<EnumSingleton> singleClass  = (Class<EnumSingleton>) Class.forName("com.demo.spring.test.java.EnumSingleton");
            Constructor<EnumSingleton> declaredConstructor = singleClass.getDeclaredConstructor(null);
            declaredConstructor.setAccessible(true);
            EnumSingleton reflixSingleton = declaredConstructor.newInstance();
            System.out.println("反射获取到实例=={}"+reflixSingleton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
