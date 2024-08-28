package com.baoge.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.util.List;

/**
 * @Author: xubao_shao@163.com
 * @Description:
 * @Date 2024/8/28 20:45
 */
public class WordUtils {

    private static InputStream inputStream;

    /**
     * 在docx文档模版中的表格中填充数据
     */
    public static void fillDataToTableInDocx() throws Exception {
        InputStream inputStream = new FileInputStream("E:/template.docx");
        XWPFDocument document = new XWPFDocument(inputStream);

        List<XWPFTable> tables = document.getTables();
        XWPFTable table = tables.get(0); // 假设我们需要填充第一个表格

        List<XWPFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) { // 从第二行开始遍历
            XWPFTableRow row = rows.get(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                // 根据单元格位置和数据源填充数据
                cell.setText("数据" + i + "-" + j);
            }
        }
        OutputStream outputStream = new FileOutputStream("output.docx");
        document.write(outputStream);
        outputStream.close();
    }

    /**
     * 在docx文档模版中生成表格并填充数据
     */
    public static void generateTableInDocx() {
        try (FileInputStream fis = new FileInputStream("E:/template.docx");
             XWPFDocument document = new XWPFDocument(fis)) {
            // 创建表格，并设置行数和列数
            XWPFTable table = document.createTable(3, 3);

            // 为每行设置数据
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                XWPFTableRow row = table.getRow(rowIndex);
                for (int colIndex = 0; colIndex < 3; colIndex++) {
                    row.getCell(colIndex).setText("Row " + (rowIndex + 1) + ", Col " + (colIndex + 1));
                }
            }

            try (FileOutputStream fos = new FileOutputStream("output.docx")) {
                document.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        fillDataToTableInDocx();

        generateTableInDocx();
    }

}
