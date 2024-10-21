package com.baoge.service;

import com.baoge.model.AcctScheduleJob;
import org.quartz.SchedulerException;

import java.util.List;

public interface ScheduleJobService {

    /**
     * 添加任务
     *
     * @param job
     * @throws SchedulerException
     */
    void addJob(AcctScheduleJob job) throws SchedulerException;

    /**
     * 获取所有任务
     *
     * @return
     */
    List<AcctScheduleJob> getAllJob() throws SchedulerException;

    /**
     * 所有正在运行的任务
     *
     * @return
     */
    List<AcctScheduleJob> getRunningJob() throws SchedulerException;

    /**
     * 暂停一个任务
     *
     * @param AcctScheduleJob
     * @throws SchedulerException
     */
    void pauseJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException;

    void resumeJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException;

    /**
     * 删除一个任务
     *
     * @param AcctScheduleJob
     * @throws SchedulerException
     */
    void deleteJob(AcctScheduleJob AcctScheduleJob) throws SchedulerException;

    /**
     * 更新表达式
     *
     * @param AcctScheduleJob
     * @throws SchedulerException
     */
    void updateJobCron(AcctScheduleJob AcctScheduleJob) throws SchedulerException;

}
