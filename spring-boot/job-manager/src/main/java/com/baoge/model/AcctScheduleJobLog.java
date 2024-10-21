package com.baoge.model;

import lombok.Data;

/**
 * @TableName acct_schedule_job_log
 */
@Data
public class AcctScheduleJobLog {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务执行时调用哪个类的方法 包名+类名，全路径
     */
    private String beanClass;

    /**
     * 任务执行开始时间
     */
    private String jobStart;

    /**
     * 任务执行结束时间
     */
    private String jobEnd;

    /**
     * 异常信息
     */
    private String exceptionInfo;
}