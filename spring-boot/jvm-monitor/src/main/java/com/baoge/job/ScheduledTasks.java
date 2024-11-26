package com.baoge.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
@Component
public class ScheduledTasks {

    /**
     * 从0秒开始，每20分钟（第五分钟开始，每隔二十分钟），在每小时的开始时刻（0分钟），每天，每月，每星期的日期执行。
     */
    @Scheduled(cron = "0 5/15 0/1 * * ?")
    public void performTask() {
        // 定时任务的内容
        System.out.println("执行定时任务：" + System.currentTimeMillis());
    }
}