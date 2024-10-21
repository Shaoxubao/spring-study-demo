package com.baoge.job.init;

import com.baoge.mapper.AcctScheduleJobMapper;
import com.baoge.model.AcctScheduleJob;
import com.baoge.service.ScheduleJobService;
import com.baoge.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化定时任务
 */
@Component
public class InitJob implements ApplicationRunner {

    @Autowired
    private ScheduleJobService service;
    @Autowired
    private AcctScheduleJobMapper acctScheduleJobMapper;
    @Value("${task_sort:}")
    private String task_sort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogUtils.info("实例化List<ScheduleJob>, 从数据库读取....【需要执行的定时任务】", this);
        Integer taskSort = null;
        if (StringUtils.isNotEmpty(task_sort)) {
            taskSort = Integer.valueOf(task_sort);
        }
        List<AcctScheduleJob> acctScheduleJobList = acctScheduleJobMapper.getInitJob(1, taskSort);
        for (AcctScheduleJob job : acctScheduleJobList) {
            service.addJob(job);
        }
        System.out.println("执行方法");
    }

}
