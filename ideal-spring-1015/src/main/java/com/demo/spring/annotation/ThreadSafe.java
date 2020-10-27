package com.demo.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 学习模块中用来标记【线程安全的类】
 */
@Target(ElementType.TYPE) // 作用目标：type：类或接口上使用
@Retention(RetentionPolicy.SOURCE) // 该注解生命周期：source:JAVA源文件阶段，不会被编译
public @interface ThreadSafe {

    String value() default "";

}
