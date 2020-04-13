package com.demo.syn.service.impl;

import com.demo.syn.service.QuartzService;
import com.demo.syn.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;


/**
 * quartz 实现类 通过前动态的pojo类 运用反射执行需要执行的   bean/method方法
 */
@Slf4j
@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {
    private static final String METHODNAME = "execute";

    @Override
    public void executeTask(String beanName, String methodName) {
        Object object = ApplicationContextUtil.getBean(beanName);
        try {
            //log.info("[QuartzServiceImpl] 反射调beanName:{},methodName:{}法开始.........", beanName, methodName);
            if (beanName.contains("\\.")) {
                Class<?> clazz = Class.forName(beanName);
                Object cronJob = ApplicationContextUtil.getBean(clazz);
                Method method1 = clazz.getMethod(methodName);
                method1.invoke(cronJob);
            } else {
                Method method = object.getClass().getMethod(methodName);
                method.invoke(object);
            }

        } catch (Exception e) {
            log.error("[QuartzServiceImpl] method invoke error,beanName:{},methodName:{}", beanName, methodName);
            log.error("错误：", e);
            return;
        }
    }

    @Override
    public void executeTask(String beanName) {
        executeTask(beanName, METHODNAME);
    }
}
