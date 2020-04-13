package com.demo.syn.config;

import com.demo.syn.dto.ScheduleJob;
import com.demo.syn.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * spring集成Quartz有2种方法：
 * 1.  自定义类  继承JobDetailBean或者实现Job方法.
 * 2. MethodInvokeJobDetailFactoryBean. 将需要执行的定时任务注入JOB中 一般是在xml中配置
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class ScheduleTask extends QuartzJobBean {

    @Autowired
    private QuartzService quartzService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ScheduleJob object = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        //log.info("ScheduleJob的值为:" + object);
        if (object != null) {
            if (object.getMethodName() == null || object.getMethodName().equals("")) {
                quartzService.executeTask(object.getBeanName());
            } else {
                quartzService.executeTask(object.getBeanName(), object.getMethodName());
            }
        }

    }
}