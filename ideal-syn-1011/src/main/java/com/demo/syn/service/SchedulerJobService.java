package com.demo.syn.service;

import com.demo.syn.dto.Result;
import com.demo.syn.dto.ScheduleJob;
import org.quartz.SchedulerException;

public interface SchedulerJobService {
    /**
     * 获取所有的任务
     */
    Result getAllScheduleJob();

    /**
     * 判断一个任务是否为空
     *
     * @param scheduleJob 任务
     */
    Result checkNotNull(ScheduleJob scheduleJob);

    /**
     * 获取所有运行中的任务
     *
     * @return jobList 任务列表
     */
    Result getAllRunningJob();

    /**
     * 新增或更新任务
     *
     * @return jobList 任务列表
     */
    Result saveOrUpdate(ScheduleJob scheduleJob) throws Exception;

    /**
     * 停止运行任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 异常
     */
    Result pauseJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 删除一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    Result deleteJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 运行一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 任务调度异常
     */
    Result runOneJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 重启一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 异常
     */
    Result resumeJob(String jobName, String jobGroup) throws SchedulerException;


}
