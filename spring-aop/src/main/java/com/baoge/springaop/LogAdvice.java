package com.baoge.springaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * @Author shaoxubao
 * @Date 2019/10/12 16:02
 */

@Aspect
public class LogAdvice {

    @Pointcut(value = "execution(public int com.baoge.springaop.Machine.*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void logBefore() {
        System.out.println("logBefore....");
    }

    @After(value = "pointcut()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + "logAfter....");
    }

    @AfterReturning(value = "pointcut()", returning = "result")
    public void logReturn(Object result) {
        System.out.println("logReturn....返回值" + result);
    }

    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.println(joinPoint.getSignature().getName() + "logException...." + exception);
    }

    @Around(value = "pointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("logAround....proceedbefore...");
        // 这个就是方法的返回值
        Object proceed = joinPoint.proceed();
		System.out.println("logAround....proceedafter...");
        return proceed;
    }

}