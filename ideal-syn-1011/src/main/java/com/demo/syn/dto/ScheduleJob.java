package com.demo.syn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleJob implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;

    private String jobId;

    private String jobName;

    private String jobStatus;

    private String jobGroup;

    private String cronExpression;

    private String description;

    private String beanName;

    private String methodName;

    private String prevFireTime;

    private String nextFireTime;
}