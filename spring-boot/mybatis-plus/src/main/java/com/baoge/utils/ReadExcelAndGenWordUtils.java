package com.baoge.utils;


import cn.hutool.poi.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReadExcelAndGenWordUtils {

    public static void main(String[] args) {
        // 指定要读取的Excel文件路径
        String filePath = "D:\\需接入的代理用户信息表.xlsx";
        // 使用ExcelUtil工具类创建ExcelReader对象
        ExcelReader reader = cn.hutool.poi.excel.ExcelUtil.getReader(filePath);
        // 读取第一页的所有行，并获取每一行的内容（以Map形式）
        List<Map<String, Object>> read = reader.readAll(); // 0表示第一页，如果有多个sheet页，可以通过索引选择

        try {
            String templateFileName =  Constant.TEMPLATE_UTL + "templateNew.docx";
            log.info("templateFileName=" + templateFileName);
            // 模板路径
            InputStream tempFileStream = PathUtil.readFileAsStream(templateFileName);
            log.info("tempFileStream=" + tempFileStream.toString());
            // 遍历并打印每一行的内容
            for (Map<String, Object> row : read) {
                System.out.println(row); // 输出整行内容，键是列名，值是单元格内容
                ExportWordWithTemplate export = new ExportWordWithTemplate();
                // 构造数据
                HashMap<String, Object> values = new HashMap(row);
                values.put("plantName", "1222");
                export.exportDataWord(tempFileStream, null, values);
            }
        } catch (IOException e) {
            log.error("生成word失败，原因：", e);
        }
        // 关闭reader，释放资源
        reader.close();
    }
}
