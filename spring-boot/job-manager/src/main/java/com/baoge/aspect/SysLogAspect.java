package com.baoge.aspect;

import com.baoge.annotation.SysLog;
import com.baoge.mapper.AcctScheduleJobLogMapper;
import com.baoge.model.AcctScheduleJobLog;
import com.baoge.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 系统日志切面
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Autowired

    private AcctScheduleJobLogMapper acctScheduleJobLogMapper;

    @Pointcut("@annotation(com.baoge.annotation.SysLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String start = TimeUtils.getCurrentDateTime();
        String end = "";
        Object result = null;
        try {
            result = point.proceed();
            end = TimeUtils.getCurrentDateTime();
            saveLog(point, start, end, "");
        } catch (Exception e) {
            end = TimeUtils.getCurrentDateTime();
            saveLog(point, start, end, e.getMessage());
        }
        return result;
    }

    /**
     * 保留日志
     */
    private void saveLog(ProceedingJoinPoint joinPoint, String start, String end, String exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = method.getAnnotation(SysLog.class);
        AcctScheduleJobLog jobLog = new AcctScheduleJobLog();
        if (sysLog != null) {
            log.info("定时任务，执行开始时间：{},执行结束时间：{},执行任务名称：{},执行任务beanClass：{}",
                    start, end, sysLog.name(), sysLog.beanClass());
            jobLog.setName(sysLog.name());
            jobLog.setBeanClass(sysLog.beanClass());
            jobLog.setJobStart(start);
            jobLog.setJobEnd(end);
            jobLog.setExceptionInfo(exception);
            acctScheduleJobLogMapper.insertAcctScheduleJobLog(jobLog);
        }
    }
}
