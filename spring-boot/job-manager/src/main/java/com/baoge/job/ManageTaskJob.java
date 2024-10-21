package com.baoge.job;


import com.baoge.mapper.AcctScheduleJobMapper;
import com.baoge.model.AcctScheduleJob;
import com.baoge.service.ScheduleJobService;
import com.baoge.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManageTaskJob {

    @Autowired
    private ScheduleJobService service;
    @Autowired
    private AcctScheduleJobMapper acctScheduleJobMapper;
    @Value("${task_sort:}")
    private String task_sort;

    public void execute() throws SchedulerException {
        LogUtils.info("监测定时任务开启", this);
        Integer taskSort = null;
        if (StringUtils.isNotEmpty(task_sort)) {
            taskSort = Integer.valueOf(task_sort);
        }
        LogUtils.info("监测定时任务：查询要暂停的定时任务", this);
        List<AcctScheduleJob> pauseList = acctScheduleJobMapper.getInitJob(2, taskSort);
        for (AcctScheduleJob job : pauseList) {
            acctScheduleJobMapper.updateJobStatus(0, job.getId());
            service.pauseJob(job);
            LogUtils.info("监测定时任务：" + job.getName() + " 已暂停", this);
        }
        LogUtils.info("监测定时任务：查询要重启的定时任务", this);
        List<AcctScheduleJob> acctScheduleJobList = acctScheduleJobMapper.getInitJob(3, taskSort);
        for (AcctScheduleJob job : acctScheduleJobList) {
            acctScheduleJobMapper.updateJobStatus(1, job.getId());
            service.resumeJob(job);
            LogUtils.info("监测定时任务：" + job.getName() + " 已重启", this);
        }
    }
}
