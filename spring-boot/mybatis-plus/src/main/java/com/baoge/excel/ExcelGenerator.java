package com.baoge.excel;

import org.apache.poi.ss.usermodel.*;   
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
   
import java.io.FileOutputStream;   
import java.io.IOException;   
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
   
public class ExcelGenerator {

    public static void main(String[] args) {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();

        // 创建工作表
        Sheet sheet = workbook.createSheet("虚拟电厂能力校核清单");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        String[] headers = {"序号", "虚拟电厂名称", "测试申请日期", "测试通过日期", "调峰与需求响应校核上调能力（MW）", "调峰与需求响应校核下调能力（MW）", "现货场景校核能力（MW）", "户号", "户名", "报装容量（MW）", "所属行业", "行业可调节容量（MW）", "校核最大上调能力总和（MW）", "校核最大下调能力总和（MW）"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 示例数据
        List<List<Object>> data = new ArrayList<>();
        data.add(Arrays.asList(1, "大唐陕西能源营销有限公司", "2025-08-20", "2025-08-28", 1.41, 1.5, 2.6, "6102202025289", "五得利集团咸阳面粉有限公司", 7.4, "制造业", 2.02, 8.36, 8.36));
        data.add(Arrays.asList(1, "大唐陕西能源营销有限公司", "2025-08-18", "2025-08-21", 5.06, 3.85, 5.76, "6103022800043", "汉中西乡尧柏水泥有限公司", 24.5, "制造业", 6.7, 8.36, 8.36));
        data.add(Arrays.asList(2, "陕西榆林能源集团售电有限公司", "2025-07-03", "2025-07-07", 3.14, 3.89, 3.6, "6103112285961", "靖边县山水水泥有限公司", 6.45, "制造业", 1.76, 8.36, 8.36));
        data.add(Arrays.asList(2, "陕西榆林能源集团售电有限公司", "2025-07-03", "2025-07-07", 3.14, 3.89, 3.6, "6108067712464", "靖边县耀隆实业有限公司2号", 0.5, "制造业", 0.14, 7.46, 7.46));
        data.add(Arrays.asList(2, "陕西榆林能源集团售电有限公司", "2025-07-03", "2025-07-07", 3.14, 3.89, 3.6, "6108067712463", "靖边县耀隆实业有限公司1号", 0.1, "采矿业", 0.03, 7.46, 7.46));
        data.add(Arrays.asList(2, "陕西榆林能源集团售电有限公司", "2025-07-03", "2025-07-07", 3.14, 3.89, 3.6, "6108067712465", "靖边县耀隆实业有限公司3号", 0.5, "制造业", 0.14, 7.46, 7.46));
        data.add(Arrays.asList(2, "陕西榆林能源集团售电有限公司", "2025-06-30", "2025-07-03", 3.69, 2.33, 3.86, "6108615300043", "榆林山水水泥有限公司", 10.63, "制造业", 2.73, 7.46, 7.46));

        // 填充数据
        int rowNum = 1;
        for (List<Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.size(); i++) {
                Cell cell = row.createCell(i);
                Object value = rowData.get(i);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入文件
        try (FileOutputStream outputStream = new FileOutputStream("虚拟电厂能力校核清单.xlsx")) {
            workbook.write(outputStream);
            System.out.println("Excel 文件生成成功！");
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
}
