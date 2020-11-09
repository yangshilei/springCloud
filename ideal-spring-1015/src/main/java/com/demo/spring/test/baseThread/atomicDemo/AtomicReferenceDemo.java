package com.demo.spring.test.baseThread.atomicDemo;

import com.demo.spring.annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class AtomicReferenceDemo {

    public static void main(String[] args) {
        AtomicReference<String> name = new AtomicReference<>();
        name.set("tom");
        System.out.println("1."+name.get());

        name.compareAndSet("tom","i love you");
        System.out.println("2."+name.get());

        name.compareAndSet("jack","hello");
        System.out.println("3."+name.get());

        name.compareAndSet("i love you","hehe");
        System.out.println("4."+name.get());

        System.out.println("a".hashCode());
    }

}
