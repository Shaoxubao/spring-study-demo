package com.baoge.task;

import com.baoge.model.AcctScheduleJob;
import com.baoge.utils.TaskUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJobFactory implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        AcctScheduleJob scheduleJob = (AcctScheduleJob) context.getMergedJobDataMap().get("acctScheduleJob");
        TaskUtils.invokeMethod(scheduleJob);
    }
}