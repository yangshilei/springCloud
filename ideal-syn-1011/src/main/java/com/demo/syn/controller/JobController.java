package com.demo.syn.controller;

import com.demo.syn.constants.CommConstants;
import com.demo.syn.dto.Result;
import com.demo.syn.dto.ScheduleJob;
import com.demo.syn.service.SchedulerJobService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 定时任务控制器
 */
@RequestMapping("/job/manage")
@RestController
@Slf4j
public class JobController {
    private final SchedulerJobService schedulerJobService;

    @Autowired
    public JobController(SchedulerJobService schedulerJobService) {
        this.schedulerJobService = schedulerJobService;
    }

    /**
     * 获取所有的任务
     */
    @ApiOperation(value = "获取所有的任务",notes = "获取所有的任务")
    @GetMapping("/getAllJobs")
    public Result getAllJobs() {
        return schedulerJobService.getAllScheduleJob();
    }

    /**
     * 获取正在执行的任务列表
     */
    @ApiOperation(value = "获取正在执行的任务列表",notes = "获取正在执行的任务列表")
    @GetMapping("/getRunJob")
    public Result getAllRunningJob() {
        return schedulerJobService.getAllRunningJob();
    }

    /**
     * 更新或者添加一个任务
     * {
     *     "jobName":"发大发",
     *     "jobGroup":"发发发",
     *     "beanName":"sss",
     *     "methodName":"ssss",
     *     "cronExpression":"* 3/5 * * * ? *",
     *     "description":"fadfafaf"
     * }
     */
    @ApiOperation(value = "更新或者添加一个任务",notes = "更新或者添加一个任务")
    @PostMapping("/saveOrUpdate")
    public Result addOrUpdateJob(@RequestBody ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        try {
            return schedulerJobService.saveOrUpdate(scheduleJob);
        } catch (Exception e) {
            log.error("[JobController] addOrUpdateJob is failure in method:addOrUpdateJob！");
            return result;
        }
    }

    /**
     * 运行一个任务
     * {
     *     "jobName":"hello",
     *     "jobGroup":"001"
     * }
     */
    @ApiOperation(value = "运行一个任务",notes = "运行一个任务")
    @PostMapping("/runOneJob")
    public Result runJob(@RequestBody ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        try {
            return schedulerJobService.runOneJob(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        } catch (SchedulerException e) {
            log.error("[JobController] runOnejob is failure in method:runJob");
            result.setMessage("运行出错");
            return result;
        }
    }

    /**
     * 停止一个定时任务
     */
    @ApiOperation(value = "停止一个定时任务",notes = "停止一个定时任务")
    @PostMapping("/pauseJob")
    public Result pauseJob(@RequestBody ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        try {
            return schedulerJobService.pauseJob(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        } catch (SchedulerException e) {
            log.error("[JobController] pauseJob is failure in method:pauseJob");
            result.setMessage("停止任务出错");
            return result;
        }
    }

    /**
     * 删除一个定时任务
     * {
     *     "jobName":"hello",
     *     "jobGroup":"001"
     * }
     */
    @ApiOperation(value = "删除一个定时任务",notes = "删除一个定时任务")
    @DeleteMapping("/deleteJob")
    public Result deleteJob(@RequestBody ScheduleJob scheduleJob) {
        Result result = new Result(true, CommConstants.RESULT_CODE.SUCCESS);
        try {
            return schedulerJobService.deleteJob(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        } catch (SchedulerException e) {
            log.error("[JobController] deleteJob is failre in method: deleteJob!");
            result.setMessage("删除任务出错");
            return result;
        }
    }

    /**
     * 重启一个定时任务
     */
    @ApiOperation(value = "重启一个定时任务",notes = "重启一个定时任务")
    @PostMapping("/resumeJob")
    public Result resumeJob(@RequestBody ScheduleJob scheduleJob) {
        Result result = new Result(false, CommConstants.RESULT_CODE.ERROR);
        try {
            return schedulerJobService.resumeJob(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        } catch (SchedulerException e) {
            log.error("[JobController] resumeJob is failre in method: resumeJob!");
            result.setMessage("重启任务出错");
            return result;
        }
    }


}
