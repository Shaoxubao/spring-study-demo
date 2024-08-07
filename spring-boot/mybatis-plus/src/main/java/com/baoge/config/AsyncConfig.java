package com.baoge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @EnableAsync: 开启异步事件的支持
 * 然后在定时任务的类或者方法上添加@Async 。最后重启项目，每一个任务都是在不同的线程中。
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    /**
     *此处成员变量应该使用@Value从配置中读取
     */
   private int corePoolSize = 10;
   private int maxPoolSize = 200;
   private int queueCapacity = 10;
 
   @Bean
   public Executor taskExecutor() {
       ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
       executor.setCorePoolSize(corePoolSize);
       executor.setMaxPoolSize(maxPoolSize);
       executor.setQueueCapacity(queueCapacity);
       executor.initialize();
       return executor;
   }
 
}