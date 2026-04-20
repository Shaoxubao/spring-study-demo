package com.baoge.weather;

import org.apache.commons.lang3.StringUtils;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            // 读取配置
            Properties prop = new Properties();
            InputStream is = Main.class.getClassLoader().getResourceAsStream("db.properties");
            prop.load(is);
            String excelPath = prop.getProperty("excel.data.path");
            if (StringUtils.isBlank(excelPath)) {
                System.err.println("请在 db.properties 中配置 excel.data.path");
                return;
            }
            // 启动导入
            WeatherExcelImporter importer = new WeatherExcelImporter(excelPath);
            importer.importAll();
            System.out.println("导入任务完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}