package com.demo.spring.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: java堆内存溢出测试
 * @Author: yangshilei
 * 为了方便测试：需要将idea或者eclipse的-Xms和-Xmx参数均设置为20M；
 * 1.-Xms:堆的最小值
 * 2.-Xmx:堆的最大值
 * 3.-Xms的值等于-Xmx表示避免堆内存自动扩展
 */
public class HeapOutOfMemory {

    static class OOMObject{
        public OOMObject(){
            System.out.println("hello world");
        }
    }

    public static void main(String[] args) {

        List<OOMObject> list = new ArrayList<>();

        // 在无限循环中不停的创建对象实例
        while (true){
            list.add(new OOMObject());
        }
    }

}
