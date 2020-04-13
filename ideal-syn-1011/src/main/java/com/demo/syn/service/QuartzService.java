package com.demo.syn.service;

public interface QuartzService {

    /**
     * 执行任务
     *
     * @param beanName   实体类
     * @param methodName 方法名
     */
    void executeTask(String beanName, String methodName);

    /**
     * 执行任务
     *
     * @param beanName 实体类
     */
    void executeTask(String beanName);
}
