package com.baoge.service.impl;


import com.baoge.model.AcctScheduleJob;
import com.baoge.service.ScheduleJobService;
import com.baoge.task.QuartzJobFactory;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Override
    public void addJob(AcctScheduleJob job) throws SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        LogUtils.info(scheduler + ".......................................................................................add");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroupCode());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // 不存在，创建一个
        if (null == trigger) {
            Class clazz = QuartzJobFactory.class;

            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getName(), job.getGroupCode()).build();

            jobDetail.getJobDataMap().put("acctScheduleJob", job);

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCorn());

            trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroupCode()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCorn());

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }


    @Override
    public List<AcctScheduleJob> getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<AcctScheduleJob> jobList = new ArrayList<AcctScheduleJob>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                AcctScheduleJob job = new AcctScheduleJob();
                job.setName(jobKey.getName());
                job.setGroupCode(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setStatus(Integer.parseInt(triggerState.name()));
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCorn(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }


    @Override
    public List<AcctScheduleJob> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<AcctScheduleJob> jobList = new ArrayList<AcctScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            AcctScheduleJob job = new AcctScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setName(jobKey.getName());
            job.setGroupCode(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setStatus(Integer.parseInt(triggerState.name()));
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCorn(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    @Override
    public void pauseJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(AcctScheduleJob.getName(), AcctScheduleJob.getGroupCode());
        scheduler.pauseJob(jobKey);
    }


    public void resumeJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(AcctScheduleJob.getName(), AcctScheduleJob.getGroupCode());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(AcctScheduleJob.getName(), AcctScheduleJob.getGroupCode());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param AcctScheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(AcctScheduleJob AcctScheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(AcctScheduleJob.getName(), AcctScheduleJob.getGroupCode());
        scheduler.triggerJob(jobKey);
    }


    @Override
    public void updateJobCron(AcctScheduleJob AcctScheduleJob) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        TriggerKey triggerKey = TriggerKey.triggerKey(AcctScheduleJob.getName(), AcctScheduleJob.getGroupCode());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(AcctScheduleJob.getCorn());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}
