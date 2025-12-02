package com.baoge.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 数据模型类 - 用户信息
class UserInfo2 {
    private String userId;
    private String userName;
    private double reportCapacity;
    private String industry;
    private double industryAdjustableCapacity;

    // 构造函数、getter和setter
    public UserInfo2(String userId, String userName, double reportCapacity, String industry, double industryAdjustableCapacity) {
        this.userId = userId;
        this.userName = userName;
        this.reportCapacity = reportCapacity;
        this.industry = industry;
        this.industryAdjustableCapacity = industryAdjustableCapacity;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public double getReportCapacity() { return reportCapacity; }
    public String getIndustry() { return industry; }
    public double getIndustryAdjustableCapacity() { return industryAdjustableCapacity; }
}

// 数据模型类 - 测试记录
class TestRecord2 {
    private String applyDate;
    private String passDate;
    private double upAdjustCapacity;
    private double downAdjustCapacity;
    private double onSiteVerifyCapacity;
    private double checkedMaxUpCapacity;
    private double checkedMaxDownCapacity;
    private List<UserInfo2> userList;

    // 构造函数、getter和setter
    public TestRecord2(String applyDate, String passDate, double upAdjustCapacity, double downAdjustCapacity,
                       double onSiteVerifyCapacity, double checkedMaxUpCapacity, double checkedMaxDownCapacity,
                       List<UserInfo2> userList) {
        this.applyDate = applyDate;
        this.passDate = passDate;
        this.upAdjustCapacity = upAdjustCapacity;
        this.downAdjustCapacity = downAdjustCapacity;
        this.onSiteVerifyCapacity = onSiteVerifyCapacity;
        this.checkedMaxUpCapacity = checkedMaxUpCapacity;
        this.checkedMaxDownCapacity = checkedMaxDownCapacity;
        this.userList = userList;
    }

    public String getApplyDate() { return applyDate; }
    public String getPassDate() { return passDate; }
    public double getUpAdjustCapacity() { return upAdjustCapacity; }
    public double getDownAdjustCapacity() { return downAdjustCapacity; }
    public double getonSiteVerifyCapacity() { return onSiteVerifyCapacity; }
    public double getCheckedMaxUpCapacity() { return checkedMaxUpCapacity; }
    public double getCheckedMaxDownCapacity() { return checkedMaxDownCapacity; }
    public List<UserInfo2> getUserList() { return userList; }
}

// 数据模型类 - 虚拟电厂
class VirtualPowerPlant2 {
    private int id;
    private String name;
    private List<TestRecord2> testRecord2s;

    // 构造函数、getter和setter
    public VirtualPowerPlant2(int id, String name, List<TestRecord2> testRecord2s) {
        this.id = id;
        this.name = name;
        this.testRecord2s = testRecord2s;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<TestRecord2> getTestRecords() { return testRecord2s; }
}

public class VirtualPowerPlantExcelExporter2 {

    public static void main(String[] args) {
        // 创建测试数据
        List<VirtualPowerPlant2> data = createTestData();
        
        // 导出Excel
        exportToExcel(data, "虚拟电厂能力校核清单.xlsx");
    }

    // 创建测试数据
    private static List<VirtualPowerPlant2> createTestData() {
        List<VirtualPowerPlant2> result = new ArrayList<>();
        
        // 创建第一个虚拟电厂的用户
        List<UserInfo2> users1 = Arrays.asList(
            new UserInfo2("6102202052829", "五得利集团咸阳面粉有限公司", 0.74, "制造业", 0.2),
            new UserInfo2("610302800043", "汉中西乡尧柏水泥有限公司", 2.45, "制造业", 0.67)
        );
        
        // 创建第一个虚拟电厂的测试记录
        List<TestRecord2> tests1 = Arrays.asList(
            new TestRecord2("2025-08-20", "2025-08-28", 0.14, 0.15, 0.26, 0.65, 0.54, users1),
            new TestRecord2("2025-08-18", "2025-08-21", 0.51, 0.39, 0.58, 0, 0, new ArrayList<>())
        );
        
        // 添加第一个虚拟电厂
        result.add(new VirtualPowerPlant2(1, "大唐陕西能源营销有限公司", tests1));
        
        // 创建第二个虚拟电厂的用户
        List<UserInfo2> users2 = Arrays.asList(
            new UserInfo2("6108067712464", "靖边县雅曜实业有限公司2号", 0.06, "制造业", 0.01),
            new UserInfo2("6108067712463", "靖边县雅曜实业有限公司1号", 0.01, "采矿业", 0.003),
            new UserInfo2("6108067712465", "靖边县雅曜实业有限公司3号", 0.05, "制造业", 0.01)
        );
        
        // 创建第二个虚拟电厂的测试记录
        List<TestRecord2> tests2 = Arrays.asList(
            new TestRecord2("2025-07-03", "2025-07-07", 0.31, 0.39, 0.36, 0.68, 0.62, users2)
        );
        
        // 添加第二个虚拟电厂
        result.add(new VirtualPowerPlant2(2, "陕西榆林能源集团国电有限公司", tests2));
        
        return result;
    }

    // 导出到Excel
    private static void exportToExcel(List<VirtualPowerPlant2> data, String fileName) {
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建工作表
            Sheet sheet = workbook.createSheet("虚拟电厂能力校核清单");
            
            // 创建标题行
            createHeaderRow(workbook, sheet);
            
            // 填充数据
            int rowIndex = 1; // 从第二行开始(0是标题行)
            for (VirtualPowerPlant2 plant : data) {
                // 计算该虚拟电厂需要占用的行数
                int plantRowCount = 0;
                for (TestRecord2 record : plant.getTestRecords()) {
                    plantRowCount += Math.max(1, record.getUserList().size());
                }
                
                // 合并虚拟电厂名称单元格
                if (plantRowCount > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(
                        rowIndex, rowIndex + plantRowCount - 1,
                        0, 0 // 第0列
                    ));
                    sheet.addMergedRegion(new CellRangeAddress(
                        rowIndex, rowIndex + plantRowCount - 1,
                        1, 1 // 第1列
                    ));
                }
                
                // 处理每条测试记录
                for (TestRecord2 record : plant.getTestRecords()) {
                    // 计算该测试记录需要占用的行数
                    int recordRowCount = Math.max(1, record.getUserList().size());
                    
                    // 合并测试日期相关单元格
                    if (recordRowCount > 1) {
                        for (int col = 2; col <= 6; col++) { // 测试日期相关的列
                            sheet.addMergedRegion(new CellRangeAddress(
                                rowIndex, rowIndex + recordRowCount - 1,
                                col, col
                            ));
                        }
                        // 合并校核结果相关单元格
                        sheet.addMergedRegion(new CellRangeAddress(
                            rowIndex, rowIndex + recordRowCount - 1,
                            10, 10 // 第10列
                        ));
                        sheet.addMergedRegion(new CellRangeAddress(
                            rowIndex, rowIndex + recordRowCount - 1,
                            11, 11 // 第11列
                        ));
                    }
                    
                    // 填充测试记录数据和用户数据
                    List<UserInfo2> users = record.getUserList();
                    if (users.isEmpty()) {
                        // 如果没有用户，创建一行
                        Row row = sheet.createRow(rowIndex);
                        fillPlantInfo(row, plant, true);
                        fillTestRecordInfo(row, record, true);
                        rowIndex++;
                    } else {
                        // 为每个用户创建一行
                        for (int i = 0; i < users.size(); i++) {
                            Row row = sheet.createRow(rowIndex);
                            // 只在第一行填充电厂信息和测试记录信息
                            fillPlantInfo(row, plant, i == 0);
                            fillTestRecordInfo(row, record, i == 0);
                            fillUserInfo(row, users.get(i));
                            rowIndex++;
                        }
                    }
                }
            }
            
            // 调整列宽
            for (int i = 0; i < 12; i++) {
                sheet.autoSizeColumn(i);
                // 适当增加宽度，避免内容被截断
                int width = sheet.getColumnWidth(i) + 2000;
                sheet.setColumnWidth(i, Math.min(width, 65535)); // 最大列宽限制
            }
            
            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
                System.out.println("Excel导出成功: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("导出Excel失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 创建标题行
    private static void createHeaderRow(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "序号", "虚拟电厂名称", "测试申请日期", "测试通过日期",
            "调峰与需求响应校核上调能力(万千瓦)", "调峰与需求响应校核下调能力(万千瓦)",
            "现货场景校核能力(万千瓦)", "户号", "户名", "报装容量(万千瓦)",
            "所属行业", "行业可调节容量(万千瓦)", "校核最大上调能力总和(万千瓦)", "校核最大下调能力总和(万千瓦)"
        };
        
        // 创建标题单元格样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        // 创建标题单元格
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // 填充虚拟电厂信息
    private static void fillPlantInfo(Row row, VirtualPowerPlant2 plant, boolean fillData) {
        CellStyle style = createCellStyle(row.getSheet().getWorkbook());
        
        Cell idCell = row.createCell(0);
        Cell nameCell = row.createCell(1);
        
        if (fillData) {
            idCell.setCellValue(plant.getId());
            nameCell.setCellValue(plant.getName());
        }
        
        idCell.setCellStyle(style);
        nameCell.setCellStyle(style);
    }

    // 填充测试记录信息
    private static void fillTestRecordInfo(Row row, TestRecord2 record, boolean fillData) {
        CellStyle style = createCellStyle(row.getSheet().getWorkbook());
        
        Cell applyDateCell = row.createCell(2);
        Cell passDateCell = row.createCell(3);
        Cell upAdjustCell = row.createCell(4);
        Cell downAdjustCell = row.createCell(5);
        Cell verifyCell = row.createCell(6);
        Cell maxUpCell = row.createCell(12);
        Cell maxDownCell = row.createCell(13);
        
        if (fillData) {
            applyDateCell.setCellValue(record.getApplyDate());
            passDateCell.setCellValue(record.getPassDate());
            upAdjustCell.setCellValue(record.getUpAdjustCapacity());
            downAdjustCell.setCellValue(record.getDownAdjustCapacity());
            verifyCell.setCellValue(record.getonSiteVerifyCapacity());
            maxUpCell.setCellValue(record.getCheckedMaxUpCapacity());
            maxDownCell.setCellValue(record.getCheckedMaxDownCapacity());
        }
        
        applyDateCell.setCellStyle(style);
        passDateCell.setCellStyle(style);
        upAdjustCell.setCellStyle(style);
        downAdjustCell.setCellStyle(style);
        verifyCell.setCellStyle(style);
        maxUpCell.setCellStyle(style);
        maxDownCell.setCellStyle(style);
    }

    // 填充用户信息
    private static void fillUserInfo(Row row, UserInfo2 user) {
        CellStyle style = createCellStyle(row.getSheet().getWorkbook());
        
        Cell userIdCell = row.createCell(7);
        Cell userNameCell = row.createCell(8);
        Cell reportCapacityCell = row.createCell(9);
        Cell industryCell = row.createCell(10);
        Cell industryAdjustCell = row.createCell(11);
        
        userIdCell.setCellValue(user.getUserId());
        userNameCell.setCellValue(user.getUserName());
        reportCapacityCell.setCellValue(user.getReportCapacity());
        industryCell.setCellValue(user.getIndustry());
        industryAdjustCell.setCellValue(user.getIndustryAdjustableCapacity());
        
        userIdCell.setCellStyle(style);
        userNameCell.setCellStyle(style);
        reportCapacityCell.setCellStyle(style);
        industryCell.setCellStyle(style);
        industryAdjustCell.setCellStyle(style);
    }

    // 创建单元格样式
    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}