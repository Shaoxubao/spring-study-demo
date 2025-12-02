package com.baoge.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 数据模型类（保持不变）
class User {
    private String userId;
    private String userName;
    private double reportCapacity;
    private String industry;
    private double industryAdjustableCapacity;

    public User(String userId, String userName, double reportCapacity, String industry, double industryAdjustableCapacity) {
        this.userId = userId;
        this.userName = userName;
        this.reportCapacity = reportCapacity;
        this.industry = industry;
        this.industryAdjustableCapacity = industryAdjustableCapacity;
    }

    // getter方法（确保所有字段都能正确获取）
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public double getReportCapacity() { return reportCapacity; }
    public String getIndustry() { return industry; }
    public double getIndustryAdjustableCapacity() { return industryAdjustableCapacity; }
}

class TestRecord3 {
    private String applyDate;
    private String passDate;
    private double upRegulation;
    private double downRegulation;
    private double onSiteCapacity;
    private List<User> users;

    public TestRecord3(String applyDate, String passDate, double upRegulation, double downRegulation, double onSiteCapacity) {
        this.applyDate = applyDate;
        this.passDate = passDate;
        this.upRegulation = upRegulation;
        this.downRegulation = downRegulation;
        this.onSiteCapacity = onSiteCapacity;
        this.users = new ArrayList<>();
    }

    public void addUser(User user) { users.add(user); }
    public List<User> getUsers() { return users; }
    public String getApplyDate() { return applyDate; }
    public String getPassDate() { return passDate; }
    public double getUpRegulation() { return upRegulation; }
    public double getDownRegulation() { return downRegulation; }
    public double getOnSiteCapacity() { return onSiteCapacity; }
}

class VirtualPowerPlant3 {
    private int id;
    private String name;
    private List<TestRecord3> testRecords;

    /**
     * 校核最大上调能力总和(万千瓦)
     */
    private Double abilityUpMax;

    /**
     * 校核最大下调能力总和(万千瓦)
     */
    private Double abilityDownMax;

    public VirtualPowerPlant3(int id, String name, Double abilityUpMax, Double abilityDownMax) {
        this.id = id;
        this.name = name;
        this.abilityUpMax = abilityUpMax;
        this.abilityDownMax = abilityDownMax;
        this.testRecords = new ArrayList<>();
    }

    public void addTestRecord(TestRecord3 record) { testRecords.add(record); }
    public List<TestRecord3> getTestRecords() { return testRecords; }
    public int getId() { return id; }
    public String getName() { return name; }
    public Double getAbilityUpMax() { return abilityUpMax; }
    public Double getAbilityDownMax() { return abilityDownMax; }
}

public class VirtualPowerPlantExcelExporter3 {
    public static void main(String[] args) {
        try {
            // 1. 创建测试数据（确保数据非空）
            List<VirtualPowerPlant3> vppList = createTestData();
            System.out.println("测试数据创建完成，共" + vppList.size() + "个虚拟电厂");

            // 2. 导出Excel（指定明确的文件路径，避免路径问题）
            String filePath = System.getProperty("user.dir") + File.separator + "虚拟电厂能力校核清单.xlsx";
            exportToExcel(vppList, filePath);

            System.out.println("Excel导出成功！文件路径：" + filePath);
        } catch (Exception e) {
            System.err.println("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 确保测试数据一定非空
    private static List<VirtualPowerPlant3> createTestData() {
        List<VirtualPowerPlant3> vppList = new ArrayList<>();

        // 第一个虚拟电厂（2个测试记录，包含多用户和单用户场景）
        VirtualPowerPlant3 vpp1 = new VirtualPowerPlant3(1, "大唐陕西能源营销有限公司", 0.26, 0.25);
        // 测试记录1（2个用户）
        TestRecord3 record1 = new TestRecord3("2025-08-20", "2025-08-28", 0.14, 0.15, 0.26);
        record1.addUser(new User("6102202052829", "五得利集团咸阳面粉有限公司", 0.74, "制造业", 0.2));
        record1.addUser(new User("610302800043", "汉中西乡尧柏水泥有限公司", 2.45, "制造业", 0.67));
        vpp1.addTestRecord(record1);
        // 测试记录2（1个用户）
        TestRecord3 record2 = new TestRecord3("2025-08-18", "2025-08-21", 0.51, 0.39, 0.58);
        record2.addUser(new User("6103112285961", "靖边县山水水泥有限公司", 0.65, "制造业", 0.18));
        vpp1.addTestRecord(record2);
        vppList.add(vpp1);

        // 第二个虚拟电厂（1个测试记录，2个用户）
        VirtualPowerPlant3 vpp2 = new VirtualPowerPlant3(2, "陕西榆林能源集团国电有限公司", 0.29, 0.15);
        TestRecord3 record3 = new TestRecord3("2025-07-03", "2025-07-07", 0.31, 0.39, 0.36);
        record3.addUser(new User("6108067712464", "靖边县雅唯实业有限公司2号", 0.06, "制造业", 0.01));
        record3.addUser(new User("6108067712463", "靖边县雅唯实业有限公司1号", 0.01, "采矿业", 0.003));
        vpp2.addTestRecord(record3);
        vppList.add(vpp2);

        return vppList;
    }

    private static void exportToExcel(List<VirtualPowerPlant3> vppList, String filePath) throws IOException {
        // 验证数据非空
        if (vppList == null || vppList.isEmpty()) {
            throw new IllegalArgumentException("导出数据不能为空！");
        }

        // 1. 创建工作簿和工作表（确保工作表正确初始化）
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("虚拟电厂能力校核清单");
            sheet.setDefaultRowHeightInPoints(20); // 设置默认行高，避免内容被遮挡

            // 2. 创建标题行（优先创建，确保表头存在）
            createHeaderRow(workbook, sheet);

            // 3. 写入数据（行号从1开始，标题行为0）
            int currentRowNum = 1;

            // 遍历虚拟电厂
            for (VirtualPowerPlant3 vpp : vppList) {
                // 计算当前虚拟电厂所有测试记录的总用户行数（用于合并序号和名称）
                int totalVppRows = vpp.getTestRecords().stream()
                        .mapToInt(testRecord -> testRecord.getUsers().size())
                        .sum();
                int vppStartRow = currentRowNum; // 记录虚拟电厂起始行
                boolean isFirstTestRecord = true; // 标记是否是当前虚拟电厂的第一个测试记录

                // 遍历当前虚拟电厂的测试记录
                for (TestRecord3 testRecord : vpp.getTestRecords()) {
                    List<User> users = testRecord.getUsers();
                    int userCount = users.size();

                    // 为每个用户创建行
                    for (int i = 0; i < userCount; i++) {
                        User user = users.get(i);
                        Row row = sheet.createRow(currentRowNum + i); // 确保行号连续

                        // 3.1 虚拟电厂相关列（0：序号，1：名称）- 合并整个虚拟电厂的所有行
                        if (i == 0 && isFirstTestRecord) {
                            // 序号 - 合并虚拟电厂所有行
                            Cell vppIdCell = row.createCell(0);
                            vppIdCell.setCellValue(vpp.getId());
                            sheet.addMergedRegion(new CellRangeAddress(
                                    vppStartRow, vppStartRow + totalVppRows - 1, 0, 0
                            ));

                            // 虚拟电厂名称 - 合并虚拟电厂所有行
                            Cell vppNameCell = row.createCell(1);
                            vppNameCell.setCellValue(vpp.getName());
                            sheet.addMergedRegion(new CellRangeAddress(
                                    vppStartRow, vppStartRow + totalVppRows - 1, 1, 1
                            ));

                            // 上调总和
                            Cell upSumCell = row.createCell(12);
                            upSumCell.setCellValue(vpp.getAbilityUpMax());
                            sheet.addMergedRegion(new CellRangeAddress(
                                    vppStartRow, vppStartRow + totalVppRows - 1, 12, 12
                            ));
                            // 下调总和
                            Cell downSumCell = row.createCell(13);
                            downSumCell.setCellValue(vpp.getAbilityDownMax());
                            sheet.addMergedRegion(new CellRangeAddress(
                                    vppStartRow, vppStartRow + totalVppRows - 1, 13, 13
                            ));
                        }

                        // 3.2 测试记录相关列（2-6列）- 保持按测试记录合并逻辑
                        if (i == 0) {
                            // 申请日期
                            Cell applyDateCell = row.createCell(2);
                            applyDateCell.setCellValue(testRecord.getApplyDate());
                            if (userCount > 1) sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + userCount - 1, 2, 2));

                            // 通过日期
                            Cell passDateCell = row.createCell(3);
                            passDateCell.setCellValue(testRecord.getPassDate());
                            if (userCount > 1) sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + userCount - 1, 3, 3));

                            // 上调能力
                            Cell upRegCell = row.createCell(4);
                            upRegCell.setCellValue(testRecord.getUpRegulation());
                            if (userCount > 1) sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + userCount - 1, 4, 4));

                            // 下调能力
                            Cell downRegCell = row.createCell(5);
                            downRegCell.setCellValue(testRecord.getDownRegulation());
                            if (userCount > 1) sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + userCount - 1, 5, 5));

                            // 现场校验能力
                            Cell onSiteCell = row.createCell(6);
                            onSiteCell.setCellValue(testRecord.getOnSiteCapacity());
                            if (userCount > 1) sheet.addMergedRegion(new CellRangeAddress(currentRowNum, currentRowNum + userCount - 1, 6, 6));
                        }

                        // 3.3 用户相关列（7-11列，必写，确保数据非空）
                        row.createCell(7).setCellValue(user.getUserId()); // 户号
                        row.createCell(8).setCellValue(user.getUserName()); // 户名
                        row.createCell(9).setCellValue(user.getReportCapacity()); // 报装容量
                        row.createCell(10).setCellValue(user.getIndustry()); // 所属行业
                        row.createCell(11).setCellValue(user.getIndustryAdjustableCapacity()); // 可调节容量

                        // 3.4 应用单元格样式
                        applyDataCellStyle(workbook, row);
                    }

                    // 处理完第一个测试记录后更新标志
                    if (isFirstTestRecord) {
                        isFirstTestRecord = false;
                    }

                    // 更新行号，确保下一个测试记录从正确位置开始
                    currentRowNum += userCount;
                }
            }

            // 4. 调整列宽（确保所有列都能显示完整）
            adjustColumnWidth(sheet);

            // 5. 写入文件（确保文件流正确刷新）
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                fos.flush(); // 强制刷新，确保数据完全写入
            }
        }
    }

    // 创建标题行（确保表头样式正确，内容非空）
    private static void createHeaderRow(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "序号", "虚拟电厂名称", "测试申请日期", "测试通过日期",
                "调峰与需求响应校核上调能力(万千瓦)", "调峰与需求响应校核下调能力(万千瓦)", "现货场景校核能力(万千瓦)",
                "户号", "户名", "报装容量(万千瓦)", "所属行业", "行业可调节容量(万千瓦)", "校核最大上调能力总和(万千瓦)",
                "校核最大下调能力总和(万千瓦)"
        };

        // 标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 写入标题
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    // 应用数据行样式（简化逻辑，避免空指针）
    private static void applyDataCellStyle(Workbook workbook, Row row) {
        CellStyle style = workbook.createCellStyle();
        // 边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 对齐
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 为所有单元格应用样式（确保没有空单元格）
        for (int i = 0; i < 14; i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
                cell = row.createCell(i);
            }
            cell.setCellStyle(style);
        }
    }

    // 调整列宽（确保所有列都能显示完整内容）
    private static void adjustColumnWidth(Sheet sheet) {
        // 列宽数组，对应14列，根据内容长度设置初始宽度
        int[] columnWidths = {8, 25, 15, 15, 18, 18, 20, 18, 25, 18, 12, 22, 18, 18};
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * 256); // 256 = 1个字符宽度
        }
    }
}