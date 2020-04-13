package com.demo.syn.service.impl;

import com.demo.syn.config.ScheduleTask;
import com.demo.syn.constants.CommConstants;
import com.demo.syn.dto.Result;
import com.demo.syn.dto.ScheduleJob;
import com.demo.syn.service.SchedulerJobService;
import com.demo.syn.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义job类型实现层 （包括  新增 修改 重启 关闭 查询所有任务  查询正在执行的任务）
 */
@Slf4j
@Service("schedulerJobService")
public class SchedulerJobServiceImpl implements SchedulerJobService {

    private final Scheduler scheduler;

    @Autowired
    public SchedulerJobServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 获取所有的任务
     */
    @Override
    public Result getAllScheduleJob() {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        List<ScheduleJob> jobList = new ArrayList<>();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey key : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(key);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = getScheduleJob(key, trigger);
                    scheduleJob.setDescription(trigger.getDescription());
                    scheduleJob.setPrevFireTime(DateUtil.format(trigger.getPreviousFireTime()));
                    scheduleJob.setNextFireTime(DateUtil.format(trigger.getNextFireTime()));
                    jobList.add(scheduleJob);
                }
            }
        } catch (SchedulerException e) {
            log.error("[SchedulerJobServiceImpl] get the jobKeys is error", e);
            result.setResult(false);
            result.setErrorCode(CommConstants.RESULT_CODE.ERROR);
            result.setMessage("查询定时任务失败");
            return result;
        }
        result.setResultObject(jobList);
        return result;
    }

    /**
     * 获取所有运行中的任务
     *
     * @return jobList 任务列表
     */
    @Override
    public Result getAllRunningJob() {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        List<JobExecutionContext> executionJobList = null;
        try {
            executionJobList = scheduler.getCurrentlyExecutingJobs();
            List<ScheduleJob> jobList = new ArrayList<>();
            for (JobExecutionContext jobExecutionContext : executionJobList) {
                JobDetail jobDetail = jobExecutionContext.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = jobExecutionContext.getTrigger();
                ScheduleJob scheduleJob = getScheduleJob(jobKey, trigger);
                jobList.add(scheduleJob);
            }
            result.setResultObject(jobList);
        } catch (SchedulerException e) {
            result.setResult(false);
            result.setErrorCode(CommConstants.RESULT_CODE.ERROR);
            result.setMessage("获取所有运行中的任务失败");
            log.error("[SchedulerJobServiceImpl] get the CurrentlyExecutingJobs is error", e);
            return result;
        }
        return result;
    }

    /**
     * 更新新的任务或者添加一个新的任务
     *
     * @param scheduleJob 任务
     * @throws Exception 异常
     */
    @Override
    public Result saveOrUpdate(ScheduleJob scheduleJob) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (cronTrigger == null) {
            return addJob(scheduleJob);
        } else {
            return updateJobCronSchedule(scheduleJob);
        }
    }

    /**
     * 停止运行任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 异常
     */
    @Override
    public Result pauseJob(String jobName, String jobGroup) throws SchedulerException {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
        result.setMessage("停止任务成功");
        return result;
    }

    /**
     * 删除一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     */
    @Override
    public Result deleteJob(String jobName, String jobGroup) throws SchedulerException {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
        result.setMessage("删除任务成功");
        return result;
    }

    /**
     * 运行一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 任务调度异常
     */
    @Override
    public Result runOneJob(String jobName, String jobGroup) throws SchedulerException {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
        result.setMessage("运行任务成功");
        return result;
    }

    /**
     * 重启一个任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 异常
     */
    @Override
    public Result resumeJob(String jobName, String jobGroup) throws SchedulerException {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
        result.setMessage("重启任务成功");
        return result;
    }


    /**
     * 添加任务
     *
     * @param scheduleJob 任务
     */
    private Result addJob(ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        if (StringUtils.isEmpty(scheduleJob.getCronExpression())) {
            result.setMessage("CronExpression不能为空");
            return result;
        }
        JobDetail jobDetail = JobBuilder.newJob(ScheduleTask.class)
                .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).build();
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(scheduleJob.getCronExpression());
        CronTrigger trigger = TriggerBuilder.newTrigger().withDescription(scheduleJob.getDescription())
                .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();

        // repeat test
        //SimpleScheduleBuilder simpleScheduleBuilder  = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
        /*SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(5, 5)).build();*/
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            result.setResult(true);
            result.setMessage("添加定时任务成功");
            result.setErrorCode(CommConstants.RESULT_CODE.SUCCESS);
            return result;
        } catch (Exception e) {
            log.error("错误：", e);
            result.setMessage("添加任务失败");
            return result;
        }

    }

    /**
     * 更新一个任务
     *
     * @param scheduleJob 任务
     * @throws Exception 异常
     */
    private Result updateJobCronSchedule(ScheduleJob scheduleJob) throws Exception {
        Result result = checkNotNull(scheduleJob);
        if (!result.isSuccess()) {
            return result;
        }
        if (StringUtils.isEmpty(scheduleJob.getCronExpression())) {
            result = new Result(false, CommConstants.RESULT_CODE.ERROR);
            result.setMessage("任务表达式不能为空");
            return result;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
        scheduler.rescheduleJob(triggerKey, cronTrigger);
        return new Result(true, CommConstants.RESULT_CODE.SUCCESS,"更新定时任务成功");
    }


    /**
     * 判断一个任务是否为空
     *
     * @param scheduleJob 任务
     */
    @Override
    public Result checkNotNull(ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        if (scheduleJob == null) {
            result.setMessage("scheduleJob is null,Please check it");
            return result;
        }
        if (StringUtils.isEmpty(scheduleJob.getJobName())) {
            result.setMessage("the jobName of scheduleJob is null,Please check it");
            return result;
        }
        if (StringUtils.isEmpty(scheduleJob.getJobGroup())) {
            result.setMessage("the jobGroup of scheduleJob is null,Please check it");
            return result;
        }
        if (StringUtils.isEmpty(scheduleJob.getBeanName())) {
            result.setMessage("the BeanName of scheduleJob is null,Please check it");
            return result;
        }
        result.setResult(true);
        result.setErrorCode(CommConstants.RESULT_CODE.SUCCESS);
        return result;
    }


    private ScheduleJob getScheduleJob(JobKey jobKey, Trigger trigger) {
        ScheduleJob scheduleJob = new ScheduleJob();
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            scheduleJob = (ScheduleJob) jobDetail.getJobDataMap().get("scheduleJob");
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduleJob.setJobStatus(triggerState.name());
            scheduleJob.setJobName(jobKey.getName());
            scheduleJob.setJobGroup(jobKey.getGroup());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                scheduleJob.setCronExpression(cronTrigger.getCronExpression());
            }

        } catch (Exception e) {
            log.error("[SchedulerJobServiceImpl] method getScheduleJob get JobDetail error:", e);
        }
        return scheduleJob;
    }

}
