package com.baoge.utils;
 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

import com.baoge.model.MonitorInfoModel;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MonitorServer {
    private static final long GB = 1024 * 1024 * 1024;
    private static final long MB = 1024 * 1024;

    private static final long B = 1024;
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");
 
    public MonitorInfoModel monitor() {
        MonitorInfoModel monitorInfoModel = new MonitorInfoModel();
 
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
 
        long usedHeapMemory = heapMemoryUsage.getUsed();
        long maxHeapMemory = heapMemoryUsage.getMax();
        long usedNonHeapMemory = nonHeapMemoryUsage.getUsed();
        long maxNonHeapMemory = nonHeapMemoryUsage.getMax();
 
        String usedHeapMemoryInfo = decimalFormat.format(1.0 * usedHeapMemory / MB) + "MB";
        String maxHeapMemoryInfo = decimalFormat.format(1.0 * maxHeapMemory / MB) + "MB";
        String usedNonHeapMemoryInfo = decimalFormat.format(1.0 * usedNonHeapMemory / MB) + "MB";
 
        String maxNonHeapMemoryInfo;
        if (maxNonHeapMemory == -1L) {
            maxNonHeapMemoryInfo = "-";
        } else {
            maxNonHeapMemoryInfo = decimalFormat.format(1.0 * maxNonHeapMemory / MB) + "MB";
        }
        monitorInfoModel.setUsedHeapMemoryInfo(usedHeapMemoryInfo);
        monitorInfoModel.setMaxHeapMemoryInfo(maxHeapMemoryInfo);
        monitorInfoModel.setUsedNonHeapMemoryInfo(usedNonHeapMemoryInfo);
        monitorInfoModel.setMaxNonHeapMemoryInfo(maxNonHeapMemoryInfo);
 
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        if (operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean opBean = (com.sun.management.OperatingSystemMXBean) operatingSystemMXBean;
            double cpuLoad = opBean.getSystemCpuLoad();
            String cpuLoadInfo = decimalFormat.format(cpuLoad * 100) + "%";
            monitorInfoModel.setCpuLoadInfo(cpuLoadInfo);
 
            double processCpuLoad = opBean.getProcessCpuLoad();
            String processCpuLoadInfo = decimalFormat.format(processCpuLoad * 100) + "%";
            monitorInfoModel.setProcessCpuLoadInfo(processCpuLoadInfo);
 
            long totalMemorySize = opBean.getTotalPhysicalMemorySize();
            long freeMemorySize = opBean.getFreePhysicalMemorySize();
 
            String totalMemoryInfo = decimalFormat.format(1.0 * totalMemorySize / GB) + "GB";
            String freeMemoryInfo = decimalFormat.format(1.0 * freeMemorySize / GB) + "GB";
            String useMemoryInfo = decimalFormat.format(1.0 * (totalMemorySize - freeMemorySize) / GB) + "GB";
            String memoryUseRatioInfo = decimalFormat.format((1.0 * (totalMemorySize - freeMemorySize) / totalMemorySize * 100)) + "%";
            monitorInfoModel.setTotalMemoryInfo(totalMemoryInfo);
            monitorInfoModel.setFreeMemoryInfo(freeMemoryInfo);
            monitorInfoModel.setUseMemoryInfo(useMemoryInfo);
            monitorInfoModel.setMemoryUseRatioInfo(memoryUseRatioInfo);
 
            long freeSwapSpaceSize = opBean.getFreeSwapSpaceSize();
            long totalSwapSpaceSize = opBean.getTotalSwapSpaceSize();
 
            String freeSwapSpaceInfo = decimalFormat.format(1.0 * freeSwapSpaceSize / GB) + "GB";
            String totalSwapSpaceInfo = decimalFormat.format(1.0 * totalSwapSpaceSize / GB) + "GB";
            String useSwapSpaceInfo = decimalFormat.format(1.0 * (totalSwapSpaceSize - freeSwapSpaceSize) / GB) + "GB";
            String swapUseRatioInfo = decimalFormat.format((1.0 * (totalSwapSpaceSize - freeSwapSpaceSize) / totalSwapSpaceSize * 100)) + "%";
            monitorInfoModel.setFreeSwapSpaceInfo(freeSwapSpaceInfo);
            monitorInfoModel.setTotalSwapSpaceInfo(totalSwapSpaceInfo);
            monitorInfoModel.setUseSwapSpaceInfo(useSwapSpaceInfo);
            monitorInfoModel.setSwapUseRatioInfo(swapUseRatioInfo);
 
            String arch = opBean.getArch();
            String name = opBean.getName();
            monitorInfoModel.setArch(arch);
            monitorInfoModel.setName(name);
        }
        // 获取系统内存信息free -m
        printMemoryUsage(monitorInfoModel);
        return monitorInfoModel;
    }

    public void printMemoryUsage( MonitorInfoModel monitorInfoModel) {
        try {
            Process process = Runtime.getRuntime().exec("free -m");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            long total = 0, used = 0, available = 0;

            // 解析free命令的输出
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Mem:")) {
                    String[] parts = line.split("\\s+");
                    total = Long.parseLong(parts[1]);
                    used = Long.parseLong(parts[2]);
                    available = Long.parseLong(parts[6]);
                    break;
                }
            }
            System.out.printf("Memory Usage: [Used: %d MB] [Available: %d MB] [Total: %d MB]%n",
                    used, available, total);

            String totalMemoryInfoNew = decimalFormat.format(1.0 * total / B) + "GB";
            String freeMemoryInfoNew = decimalFormat.format(1.0 * available / B) + "GB";
            String useMemoryInfoNew = decimalFormat.format(1.0 * used / B) + "GB";
            monitorInfoModel.setTotalMemoryInfoNew(totalMemoryInfoNew);
            monitorInfoModel.setFreeMemoryInfoNew(freeMemoryInfoNew);
            monitorInfoModel.setUseMemoryInfoNew(useMemoryInfoNew);
            // 计算内存使用率
            String memoryUseRatioInfo = decimalFormat.format((1.0 * (total - available) / total * 100)) + "%";
            monitorInfoModel.setMemoryUseRatioInfoNew(memoryUseRatioInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) throws InterruptedException {
        MonitorServer monitorServer = new MonitorServer();
        while (true) {
            MonitorInfoModel infoModel = monitorServer.monitor();
//        String jsonString = JSON.toJSONString(infoModel, SerializerFeature.PrettyFormat);
//        log.info(jsonString);
            log.info("堆内存使用情况：" +
                            "使用中的堆内存：{} " +
                            "最大堆内存：{} " +
                            "使用中的非堆内存：{} " +
                            "最大非堆内存：{}",
                    infoModel.getUsedHeapMemoryInfo(),
                    infoModel.getMaxHeapMemoryInfo(),
                    infoModel.getUsedNonHeapMemoryInfo(),
                    infoModel.getMaxNonHeapMemoryInfo());
 
            log.info("系统信息：" +
                            "系统架构：{}" +
                            "系统名称：{} " +
                            "系统使用情况：" +
                            "CPU使用率：{} " +
                            "JVM进程CPU使用率：{} " +
                            "系统总内存：{}" +
                            "使用中的内存：{} " +
                            "内存使用率：{} " +
                            "系统总交换内存：{} " +
                            "使用中的交换内存：{} " +
                            "交换内存使用率：{}",
                    infoModel.getArch(),
                    infoModel.getName(),
                    infoModel.getCpuLoadInfo(),
                    infoModel.getProcessCpuLoadInfo(),
                    infoModel.getTotalMemoryInfo(),
                    infoModel.getUseMemoryInfo(),
                    infoModel.getMemoryUseRatioInfo(),
                    infoModel.getTotalSwapSpaceInfo(),
                    infoModel.getUseSwapSpaceInfo(),
                    infoModel.getSwapUseRatioInfo());
 
            Thread.sleep(5000);
        }
 
    }
}