package com.baoge.job;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoge.entity.AppJvmMonitorInfo;
import com.baoge.model.MonitorInfoModel;
import com.baoge.service.AppJvmMonitorInfoService;
import com.baoge.utils.MonitorServer;
import com.baoge.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledService {

    @Value("${app.name}")
    private String appName;

    @Autowired
    private AppJvmMonitorInfoService appJvmMonitorInfoService;

//   @Scheduled(cron = "0/5 * * * * *")
   public void scheduled(){
       log.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
   }
//   @Scheduled(fixedRate = 5000)
   public void scheduled1() {
       log.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
   }
//   @Scheduled(fixedDelay = 5000)
   public void scheduled2() {
       log.info("=====>>>>>fixedDelay{}",System.currentTimeMillis());
   }

    @Scheduled(cron = "0/30 * * * * *")
    public void jvmMonitor(){
        log.info("jvm monitor start-----------------------");

        MonitorServer monitorServer = new MonitorServer();
        MonitorInfoModel infoModel = monitorServer.monitor();

        AppJvmMonitorInfo info = new AppJvmMonitorInfo();
        info.setAppName(appName);
        info.setDataDate(TimeUtils.converToDate(TimeUtils.getCurrentDate() + " 00:00:00"));
        info.setCreateTime(DateUtil.date());
        info.setCpuLoad(infoModel.getCpuLoadInfo());
        info.setTotalMemory(infoModel.getTotalMemoryInfo());
        info.setFreeMemory(infoModel.getFreeMemoryInfo());
        info.setUseMemory(infoModel.getUseMemoryInfo());
        appJvmMonitorInfoService.save(info);

        log.info("jvm monitor end-----------------------");
    }

}