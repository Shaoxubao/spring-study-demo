package com.baoge.excel;

import com.baoge.excel.bean.HouseInfo;
import com.baoge.excel.bean.TestRecord;
import com.baoge.excel.bean.VirtualPowerPlant;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelExporter {
    public static void main(String[] args) {
        // 1. 准备数据
        List<VirtualPowerPlant> plants = prepareData();

        // 2. 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("虚拟电厂能力校核清单");

            // 3. 构建大标题
            createTitle(workbook, sheet);

            // 4. 构建列标题
            createHeaderRow(workbook, sheet);

            // 5. 填充数据并处理合并单元格
            fillData(workbook, sheet, plants);

            // 6. 导出文件
            try (FileOutputStream fos = new FileOutputStream("虚拟电厂能力校核清单.xlsx")) {
                workbook.write(fos);
                System.out.println("Excel导出成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 构建大标题（合并所有列）
    private static void createTitle(Workbook workbook, Sheet sheet) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("虚拟电厂能力校核清单");

        // 合并行0的所有列（列索引0到14）
        CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, 14);
        sheet.addMergedRegion(titleRange);

        // 标题样式：加粗、居中、字号14
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);
    }

    // 构建列标题行
    private static void createHeaderRow(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(1);
        String[] headers = {
            "序号", "虚拟电厂名称", "测试申请日期", "测试通过日期", 
            "调峰与需求响应调上能力(MW)", "调峰与需求响应调下能力(MW)", 
            "现货场景校核能力(MW)", "户号", "户名", "报装容量(MW)", 
            "所属行业", "行业可调节容量(MW)", "校核最大上调能力总和(MW)", 
            "校核最大下调能力总和(MW)"
        };

        // 设置列宽（每个列宽为20个字符宽度）
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 20 * 256); // 256为Excel字符宽度单位
        }

        // 填充列标题并设置样式（加粗、灰色背景、居中）
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // 填充数据并处理合并单元格
    private static void fillData(Workbook workbook, Sheet sheet, List<VirtualPowerPlant> plants) {
        int currentRow = 2; // 数据行从第2行开始（行索引0-based）
        int serialNumber = 1; // 全局序号

        for (VirtualPowerPlant plant : plants) {
            List<TestRecord> records = plant.getTestRecords();

            for (int i = 0; i < records.size(); i++) {
                TestRecord record = records.get(i);

                // 合并“虚拟电厂名称”列：若为该电厂第一条记录，合并其所有关联行的名称列
                if (i == 0) {
                    int totalHouseRows = 0;
                    for (TestRecord r : records) {
                        totalHouseRows += r.getHouses().size(); // 统计该电厂所有户信息的行数
                    }
                    CellRangeAddress plantNameRange = new CellRangeAddress(
                        currentRow, currentRow + totalHouseRows - 1, 1, 1
                    );
                    sheet.addMergedRegion(plantNameRange);
                }

                // 构建测试基本信息行（申请日期、通过日期、调峰/现货能力等）
                Row recordRow = sheet.createRow(currentRow);
                recordRow.createCell(1).setCellValue(plant.getName());       // 虚拟电厂名称（列1）
                recordRow.createCell(2).setCellValue(record.getApplyDate());   // 测试申请日期（列2）
                recordRow.createCell(3).setCellValue(record.getPassDate());   // 测试通过日期（列3）
                recordRow.createCell(4).setCellValue(record.getUpCapacity()); // 调上能力（列4）
                recordRow.createCell(5).setCellValue(record.getDownCapacity());// 调下能力（列5）
                recordRow.createCell(6).setCellValue(record.getSpotCapacity());// 现货能力（列6）

                // 构建该测试记录下的所有户信息行
                int houseStartRow = currentRow + 1;
                for (HouseInfo house : record.getHouses()) {
                    Row houseRow = sheet.createRow(houseStartRow);
                    houseRow.createCell(0).setCellValue(serialNumber++);       // 序号（列0）
                    houseRow.createCell(7).setCellValue(house.getHouseNo());   // 户号（列8）
                    houseRow.createCell(8).setCellValue(house.getHouseName());// 户名（列9）
                    houseRow.createCell(9).setCellValue(house.getInstalledCapacity()); // 报装容量（列10）
                    houseRow.createCell(10).setCellValue(house.getIndustry());// 所属行业（列11）
                    houseRow.createCell(11).setCellValue(house.getIndustryAdjustable()); // 行业可调节容量（列12）
                    houseRow.createCell(12).setCellValue(house.getMaxUp());    // 校核最大上调（列13）
                    houseRow.createCell(13).setCellValue(house.getMaxDown());  // 校核最大下调（列14）
                    houseStartRow++;
                }

                // 合并“测试申请日期”列（同一测试记录的多户信息行共享同一日期）
                CellRangeAddress applyDateRange = new CellRangeAddress(
                    currentRow, houseStartRow - 1, 2, 2
                );
                sheet.addMergedRegion(applyDateRange);

                // 合并“测试通过日期”列（同理）
                CellRangeAddress passDateRange = new CellRangeAddress(
                    currentRow, houseStartRow - 1, 3, 3
                );
                sheet.addMergedRegion(passDateRange);

                // 移动到下一个测试记录的起始行
                currentRow = houseStartRow;
            }
        }
    }

    private static List<VirtualPowerPlant> prepareData() {
        List<VirtualPowerPlant> plants = new ArrayList<>();

        // 示例1：大唐陕西能源营销有限公司
        VirtualPowerPlant plant1 = new VirtualPowerPlant();
        plant1.setName("大唐陕西能源营销有限公司");
        List<TestRecord> records1 = new ArrayList<>();

        // 测试记录1-1
        TestRecord record1_1 = new TestRecord();
        record1_1.setApplyDate("2025-08-20");
        record1_1.setPassDate("2025-08-28");
        record1_1.setUpCapacity(1.41);
        record1_1.setDownCapacity(1.5);
        record1_1.setSpotCapacity(2.6);
        List<HouseInfo> houses1_1 = new ArrayList<>();
        HouseInfo house1_1 = new HouseInfo();
        house1_1.setHouseNo("6102202025289");
        house1_1.setHouseName("五得利集团咸阳面粉有限公司");
        house1_1.setInstalledCapacity("7.4");
        house1_1.setIndustry("制造业");
        house1_1.setIndustryAdjustable("2.02");
        house1_1.setMaxUp("8.36");
        house1_1.setMaxDown("8.36");
        houses1_1.add(house1_1);
        record1_1.setHouses(houses1_1);

        // 测试记录1-2
        TestRecord record1_2 = new TestRecord();
        record1_2.setApplyDate("2025-08-18");
        record1_2.setPassDate("2025-08-21");
        record1_2.setUpCapacity(5.06);
        record1_2.setDownCapacity(3.85);
        record1_2.setSpotCapacity(5.76);
        List<HouseInfo> houses1_2 = new ArrayList<>();
        HouseInfo house1_2 = new HouseInfo();
        house1_2.setHouseNo("6103022800043");
        house1_2.setHouseName("汉中西乡尧柏水泥有限公司");
        house1_2.setInstalledCapacity("24.5");
        house1_2.setIndustry("制造业");
        house1_2.setIndustryAdjustable("6.7");
        house1_2.setMaxUp("");   // 空值示例
        house1_2.setMaxDown("");
        houses1_2.add(house1_2);
        record1_2.setHouses(houses1_2);

        records1.add(record1_1);
        records1.add(record1_2);
        plant1.setTestRecords(records1);
        plants.add(plant1);

        // 示例2：陕西榆林能源集团售电有限公司（同理构建...）
        // ...

        return plants;
    }
}