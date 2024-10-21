package com.baoge.job;

import com.baoge.annotation.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
@Slf4j
public class TestJob {
    @SysLog(name="测试定时任务",beanClass="com.baoge.job.TestJob")
    public void execute() {
        Calendar calendar = Calendar.getInstance();
        String statDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        log.info("-----------------------------TestJob 测试定时任务 -{} 开始执行-----------------------------", statDate);
        try {
            System.out.println("===========" + statDate);;
        } catch (Exception e) {
            log.error("TestJob 测试定时任务!{}",e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
        log.info("-----------------------------TestJob 测试定时任务-执行结束!-----------------------------");
    }
}
