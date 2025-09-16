package com.baoge.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
   
import java.io.FileOutputStream;   
import java.io.IOException;
   
public class ExcelCellMerge {
    public static void main(String[] args) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet("虚拟电厂能力校核清单");

        // 示例数据（这里简单模拟一些数据，实际可从数据库等数据源获取）
        Object[][] data = {
                {"虚拟电厂能力校核清单"},
                {"序号", "虚拟电厂名称", "测试申请日期", "测试通过日期", "调峰与需求响应校核上调能力（MW）", "调峰与需求响应校核下调能力（MW）", "现货场景校核能力（MW）", "户号", "户名", "报装容量（MW）", "所属行业", "行业可调节容量（MW）", "校核最大上调能力总和（MW）", "校核最大下调能力总和（MW）"},
                {1, "大唐陕西能源营销有限公司", "2025-08-20", "2025-08-28", 1.41, 1.5, 2.6, "6102202025289", "五得利集团咸阳面粉有限公司", 7.4, "制造业", 2.02, 8.36, 8.36},
                {1, "",                       "2025-08-18", "2025-08-21", 5.06, 3.85, 5.76, "6103022800043", "汉中西乡尧柏水泥有限公司", 24.5, "制造业", 6.7, "", ""},
                {2, "陕西榆林能源集团售电有限公司", "2025-07-03", "2025-07-07", 3.14, 3.89, 3.6, "6108607712464", "靖边县耀唯实业有限公司2号", 0.5, "制造业", 0.14, 7.46, 7.46},
                {2, "", "2025-06-30", "2025-07-03", 3.69, 2.33, 3.86, "610861530045", "榆林山水水泥有限公司", 10.63, "制造业", 2.73, "", ""},
                {3, "榆林电力投资有限责任公司", "2025-04-27", "2025-04-30", 2.27, 10.92, 13.59, "610861500031", "神木市鑫福源化工有限公司锦界分公司", 13.5, "制造业", 0.27, 131.93, 131.93},
                {3, "", "2025-04-27", "2025-04-30", 50.62, 109.65, 118.34, "610861510025", "府谷县袁源镁业煤化有限责任公司", 42.5, "制造业", 11.62, "", ""},
                {4, "山西风行测控股份有限公司", "2025-04-25", "2025-04-30", 5.48, 4.17, 6.14, "6108615285089", "府谷县巨源硅铁有限公司", 72.3, "制造业", 19.76, 8.57, 8.57},
                {4, "", "2025-04-24", "2025-04-28", 1.28, 1.45, 2.43, "6102101000142", "韩城迪源建材有限责任公司", 4.45, "住宿和餐饮业", 0.34, "", ""},
                {5, "弘奎（西安）智能科技有限公司", "2025-04-09", "2025-04-12", 1.12, 2.21, 3.36, "6102503009846", "陕西聚力世恒生态水泥有限公司", 2.92, "制造业", 0.73, 3.36, 3.36},
                {6, "陕西综合能源集团有限公司", "2024-11-22", "2024-11-23", 18.1, "/", "/", "6108506611995", "宁强旭日天然气综合开发有限公司", 14.04, "电力、燃气及水的生产和供应业", 3.84, 18.1, "/"}
        };

        // 构建大标题（合并所有列）
        createTitle(workbook, sheet);
        // 构建列标题行
        createHeaderRow(workbook, sheet);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 填充数据到工作表
        for (int i = 2; i < data.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = row.createCell(j);
                if (data[i][j] instanceof String) {
                    cell.setCellValue((String) data[i][j]);
                } else if (data[i][j] instanceof Integer) {
                    cell.setCellValue((Integer) data[i][j]);
                } else if (data[i][j] instanceof Double) {
                    cell.setCellValue((Double) data[i][j]);
                }
                cell.setCellStyle(cellStyle);
            }
        }

        // 合并单元格（这里以“虚拟电厂名称”列按序号合并为例，实际可根据需求调整）
        int lastIndex = 0;
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row currentRow = sheet.getRow(i);
            Row preRow = sheet.getRow(i - 1);
            Cell currentCell = currentRow.getCell(1);
            Cell preCell = preRow.getCell(1);
            if (currentCell != null && preCell != null && currentCell.getStringCellValue().isEmpty() &&!preCell.getStringCellValue().isEmpty()) {
                int firstRow = i - 1;
                int lastRow = i;
                int firstCol = 1;
                int lastCol = 1;
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
            }
        }

        // 保存工作簿
        try (FileOutputStream fileOut = new FileOutputStream("virtual_power_plant.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setWrapText(true);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }
}
