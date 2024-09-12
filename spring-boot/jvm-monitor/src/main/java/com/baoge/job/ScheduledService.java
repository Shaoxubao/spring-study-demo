package com.baoge.job;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoge.entity.AppJvmBaseInfo;
import com.baoge.entity.AppJvmMonitorInfo;
import com.baoge.model.DiskInfo;
import com.baoge.model.MonitorInfoModel;
import com.baoge.service.AppJvmBaseInfoService;
import com.baoge.service.AppJvmMonitorInfoService;
import com.baoge.utils.DiskMonitor;
import com.baoge.utils.MonitorServer;
import com.baoge.utils.TimeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    private AppJvmBaseInfoService appJvmBaseInfoService;

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

        // 内存、cpu等使用情况
        MonitorServer monitorServer = new MonitorServer();
        MonitorInfoModel infoModel = monitorServer.monitor();

        // 磁盘使用情况
        DiskMonitor diskMonitor = new DiskMonitor();
        DiskInfo diskInfo = diskMonitor.diskMonitorInfo();

        AppJvmMonitorInfo info = new AppJvmMonitorInfo();
        info.setAppName(appName);
        info.setDataDate(TimeUtils.converToDate(TimeUtils.getCurrentDate() + " 00:00:00"));
        info.setCreateTime(DateUtil.date());
        info.setCpuLoad(infoModel.getCpuLoadInfo());
        info.setTotalMemory(infoModel.getTotalMemoryInfo());
        info.setFreeMemory(infoModel.getFreeMemoryInfo());
        info.setUseMemory(infoModel.getUseMemoryInfo());
        info.setUserMemoryPercent(infoModel.getMemoryUseRatioInfo());
        info.setDiskUsed(diskInfo.getUsed());
        info.setDiskAvailable(diskInfo.getAvailable());
        info.setDiskUsePercent(diskInfo.getUsePercent());
        appJvmMonitorInfoService.save(info);

        // 磁盘总大小
        AppJvmBaseInfo baseInfo = appJvmBaseInfoService.getOne(new QueryWrapper<AppJvmBaseInfo>().eq("app_name", appName));
        if (baseInfo != null) {
            baseInfo.setDiskSize(Integer.parseInt(diskInfo.getSize()));
            appJvmBaseInfoService.updateById(baseInfo);
        }

        log.info("jvm monitor end-----------------------");
    }

}