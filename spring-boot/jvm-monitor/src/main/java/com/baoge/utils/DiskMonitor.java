package com.baoge.utils;


import com.baoge.model.DiskInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class DiskMonitor {
    public DiskInfo diskMonitorInfo() {
        DiskInfo diskInfo = new DiskInfo();
        try {
            Process process = Runtime.getRuntime().exec("df -h");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");

                // Print out the disk usage information
                System.out.println("Filesystem: " + parts[0]);
                System.out.println("Size: " + parts[1]);
                System.out.println("Used: " + parts[2]);
                System.out.println("Available: " + parts[3]);
                System.out.println("Use%: " + parts[4]);
                System.out.println("Mounted on: " + parts[5]);

                diskInfo.setSize(parts[1]);
                diskInfo.setUsed(parts[2]);
                diskInfo.setAvailable(parts[3]);
                diskInfo.setUsePercent(parts[4]);

                if ("/".equals(parts[5])) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("diskMonitorInfo failed:", e);
        }
        return diskInfo;
    }
}
