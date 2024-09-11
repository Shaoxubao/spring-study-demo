package com.baoge.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JVMMonitor {
    public static void main(String[] args) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();

        System.out.println("Heap Memory:");
        System.out.println("Init: " + heapMemoryUsage.getInit() / 1024 / 1024 + " MB");
        System.out.println("Used: " + heapMemoryUsage.getUsed() / 1024 / 1024 + " MB");
        System.out.println("Committed: " + heapMemoryUsage.getCommitted() / 1024 / 1024 + " MB");
        System.out.println("Max: " + heapMemoryUsage.getMax() / 1024 / 1024 + " MB");
    }
}
