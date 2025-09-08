package com.baoge.utils;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据word模板填充数据生成新的word
 */
@Slf4j
public class ExportWordWithTemplate {

    /**
     * 导出word
     */
    public void exportDataWord(InputStream inputStream, HttpServletResponse response, Map<String, Object> renderData) throws IOException {
        try {
            log.info("开始生成Word..........");
            //渲染表格  动态行
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            Configure config = Configure.builder()
//                    .bind("reportPcList", policy)
//                    .bind("adjustingCapacTestList", policy)
//                    .bind("accuracyCapacTestList", policy)
//                    .bind("speedCapacTestList", policy)
//                    .bind("resptimeCapacTestList", policy)
                    .build();

            XWPFTemplate template = XWPFTemplate.compile(inputStream).render(renderData);
            log.info("生成WordXWPFTemplate.compile完成");
            //=================生成word到设置浏览默认下载地址=================
            // 设置强制下载不打开
//            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//            response.setCharacterEncoding("utf-8");
            // 设置文件名
            String  name = renderData.get("plantName") + ".docx";
            String docName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
//            response.addHeader("Content-Disposition", "attachment;filename=" + docName);
            log.info("生成Word，fileName="+docName);
//            OutputStream out = response.getOutputStream();
            File f = new File(name);
            OutputStream out = new FileOutputStream(f);
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (Exception e) {
            log.error("生成Word失败，原因:", e);
            e.printStackTrace();
        }
    }

    /**
     * 根据模板生成excel文件，输入模板为文件流
     *
     * @param fileStream 模板文件流
     * @param response     生成文件类型
     * @param fileName     导出文件件名
     * @param data       map数据
     */

    public void genExcel(InputStream fileStream, HttpServletResponse response ,String fileName, Map<String, Object> data) {
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        List<ForEachBlock> listforeachblock = new ArrayList<>();
        // 读取excel文件
        Workbook wb = null;
        InputStream is = null;
        Sheet sheet = null;
        try {
            is = fileStream;
            // 获取工作薄
            if (fileName.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                return;
            }

            // 读取第一个工作页sheet
            sheet = wb.getSheetAt(0);

            // 第一行为标题
            for (int rowindex = sheet.getFirstRowNum(); rowindex < sheet.getLastRowNum()+1; rowindex++) {
                Row row = sheet.getRow(rowindex);
                if (row == null) {
                    continue;
                }
                for (int colindex = 0; colindex < row.getLastCellNum(); colindex++) {
                    Cell cell = row.getCell(colindex);// 根据不同类型转化成字符串
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String cellstring = cell.getStringCellValue();
                        CellData celldata = new CellData();
                        celldata.parseCell(cellstring);
                        ReplaceCells(cell, celldata, data);
                        if (celldata.getCelltype() == TemplateType.LOOPBEGEN || celldata.getCelltype() == TemplateType.LOOPBEGENWITHDATA) {

                            //System.out.println(cell.getRowIndex() + "  " + cell.getColumnIndex());
                            ForEachBlock foreachblock = new ForEachBlock();
                            foreachblock.setFirstRow(row.getRowNum());
                            ArrayList<ForEachCells> RowCells = new ArrayList<>();
                            ForEachCells parm = getExcelTemplateParams(cell);
                            foreachblock.setListName(parm.getCellData().getListName());
                            RowCells.add(parm);
                            while (!cellstring.contains("}}")) {
                                colindex++;
                                if (colindex > row.getLastCellNum()) {

                                    AddRowMergedRegions(sheet, row, foreachblock);
                                    colindex = 0;
                                    foreachblock.addRow(RowCells);
                                    RowCells = new ArrayList<>();
                                    row = row.getSheet().getRow(row.getRowNum() + 1);
                                    rowindex++;
                                }
                                cell = row.getCell(colindex);
                                if (cell != null) {
                                    parm = getExcelTemplateParams(cell);
                                    RowCells.add(parm);
                                    cell.setCellType(CellType.STRING);
                                    cellstring = cell.getStringCellValue();
                                } else {
                                    cellstring = "";
                                }

                            }
                            foreachblock.setLastRow(row.getRowNum());
                            foreachblock.addRow(RowCells);
                            AddRowMergedRegions(sheet, row, foreachblock);
                            listforeachblock.add(foreachblock);
                            //System.out.println(cell.getRowIndex() + "foreach" + cell.getColumnIndex());
                            break;
                        }
                        cell.setCellType(CellType.STRING);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InsertListBlockRows(sheet, listforeachblock, data);


        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename="+fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 替换单元格的模板内容
     *
     * @param excelcell
     * @param cell
     * @param map
     */
    public static void ReplaceCells(Cell excelcell, CellData cell, Map<String, Object> map) {
        if (map != null) {
            if (cell.getCelltype() == TemplateType.ISMAPFIELD)
                excelcell.setCellValue((String) map.get(cell.getCellMapName()));

        }
    }

    /**
     * 读取合并的单元格
     *
     * @param sheet
     * @param row
     * @param foreachblock
     */
    public static void AddRowMergedRegions(Sheet sheet, Row row, ForEachBlock foreachblock) {
        for (Cell cell1 : row) {
            for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--) {
                CellRangeAddress region = sheet.getMergedRegion(i);
                if (region.isInRange(cell1.getRowIndex(), cell1.getColumnIndex())) {
                    CellRangeAddress newRegion = region.copy();
                    foreachblock.addMergedRegion(newRegion);
                }
            }
        }
    }

    /**
     * 获取模板参数
     *
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
    public static ForEachCells getExcelTemplateParams(Cell cell) {
        // TemplateSumUtils templateSumHandler=new TemplateSumUtils(cell.getSheet());
        cell.setCellType(CellType.STRING);
        String name = cell.getStringCellValue();
        name = name.trim();
        ForEachCells params = new ForEachCells(name, cell.getCellStyle(), cell.getRow().getHeight());

        // 判断是不是空
        if (CellData.NULL.equals(name)) {
            params.setName(null);
            params.setConstValue(CellData.EMPTY);
        }
        params.setRownum(cell.getRowIndex());
        params.setColnum(cell.getColumnIndex());
        CellData celldata = new CellData();
        celldata.parseCell(name);
        params.setCellData(celldata);

        return params;
    }

    /**
     * 插入循环的内容并替换或填充数据
     *
     * @param sheet
     * @param listforeachblock
     * @param map
     */
    public static void InsertListBlockRows(Sheet sheet, List<ForEachBlock> listforeachblock, Map<String, Object> map) {
        int shiftrow = 0;

        for (int i = 0; i < listforeachblock.size(); i++) {ForEachBlock feBlock = listforeachblock.get(i);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> listmap = (List<Map<String, Object>>) map.get(feBlock.getListName());
            ReplaceForEachCells(sheet, listforeachblock.get(i), shiftrow, listmap.get(0));
            for (int index = 1; index < listmap.size(); index++) {
                shiftrow += InsertBlockRows(sheet, feBlock, shiftrow);
                InsertForEachCells(sheet, feBlock, shiftrow, listmap.get(index));
            }
        }

    }

    /**
     * 插入循环的内容并替换数据
     *
     * @param sheet
     * @param foreachblock
     * @param shiftrows
     * @param map
     */
    public static void ReplaceForEachCells(Sheet sheet, ForEachBlock foreachblock, int shiftrows,
                                           Map<String, Object> map) {

        for (ArrayList<ForEachCells> celllist : foreachblock.getListForEachCells()) {
            for (ForEachCells cell : celllist) {
                Row row = sheet.getRow(cell.getRownum() + shiftrows);
                // row.setHeight(cell.getHeight());
                Cell newCell = row.getCell(cell.getColnum());
                newCell.setCellStyle(cell.getCellStyle());
                newCell.setCellValue(cell.getName());

                if (map != null) {
                    if (cell.getCellData().getCelltype() == TemplateType.ISLISTFIELD || cell.getCellData().getCelltype() == TemplateType.LOOPBEGENWITHDATA) {
                        Object o = map.get(cell.getCellData().getCellMapName());
                        String[] s = cell.getName().split("\\.");
                        if(s.length==3){
                            if (ObjectUtils.isNotEmpty(o)) {
                                newCell.setCellValue(String.valueOf(((Map)o).get(s[2])));
                            }
                        }else if (ObjectUtils.isNotEmpty(o)) {
                            newCell.setCellValue(String.valueOf(o));
                        } else {
                            newCell.setCellValue("");
                        }
                    }
                    else
                        newCell.setCellValue(cell.getCellData().getCellMapName());
                }
            }
        }

    }

    /**
     * 插入循环的内容并填充数据
     *
     * @param sheet
     * @param foreachblock
     * @param shiftrows
     * @param map
     */
    public static void InsertForEachCells(Sheet sheet, ForEachBlock foreachblock, int shiftrows,
                                          Map<String, Object> map) {

        for (ArrayList<ForEachCells> celllist : foreachblock.getListForEachCells()) {
            for (ForEachCells cell : celllist) {
                Row row = sheet.getRow(cell.getRownum() + shiftrows);
                row.setHeight(cell.getHeight());
                Cell newCell = row.createCell(cell.getColnum());
                newCell.setCellStyle(cell.getCellStyle());
                if (map != null) {
                    if (cell.getCellData().getCelltype() == TemplateType.ISLISTFIELD || cell.getCellData().getCelltype() == TemplateType.LOOPBEGENWITHDATA) {
                        Object o = map.get(cell.getCellData().getCellMapName());
                        String[] s = cell.getName().split(".");
                        if(s.length==3){
                            if (ObjectUtils.isNotEmpty(o)) {
                                newCell.setCellValue(String.valueOf(((Map)o).get(s[2])));
                            }
                        }
                        if (ObjectUtils.isNotEmpty(o)) {
                            newCell.setCellValue(String.valueOf(o));
                        }else {
                            newCell.setCellValue("");
                        }
                    }
                    else
                        newCell.setCellValue(cell.getCellData().getCellMapName());
                }

            }
        }

    }

    /**
     * 插入循环的内容
     *
     * @param sheet
     * @param foreachblock
     * @param shiftrows
     * @return
     */
    public static int InsertBlockRows(Sheet sheet, ForEachBlock foreachblock, int shiftrows) {
        int StartRow = foreachblock.getLastRow() + shiftrows;
        int Rows = foreachblock.getLastRow() - foreachblock.getFirstRow() + 1;
        InsertRows(sheet, StartRow, Rows);
        for (CellRangeAddress region : foreachblock.getListCellRangeAddress().values()) {
            int targetRowFrom = region.getFirstRow() + Rows + shiftrows;
            int targetRowTo = region.getLastRow() + Rows + shiftrows;
            CellRangeAddress newRegion = region.copy();
            newRegion.setFirstRow(targetRowFrom);
            newRegion.setFirstColumn(region.getFirstColumn());
            newRegion.setLastRow(targetRowTo);
            newRegion.setLastColumn(region.getLastColumn());
            sheet.addMergedRegion(newRegion);
        }

        return Rows;
    }

    /**
     * 插入行
     *
     * @param sheet    sheet页
     * @param StartRow 插入的起始行数
     * @param Rows     插入的行数
     */
    public static void InsertRows(Sheet sheet, int StartRow, int Rows) {

        int lastRowNo = sheet.getLastRowNum();
        if (sheet.getRow(StartRow) != null) {
            sheet.shiftRows(StartRow + 1, lastRowNo + 1, Rows, true, false);
        }

        sheet.createRow(StartRow + 1);

    }

    public static void main(String[] args) {
        ExportWordWithTemplate export = new ExportWordWithTemplate();
        try {
            String templateFileName =  Constant.TEMPLATE_UTL + "templateNew.docx";
            log.info("templateFileName=" + templateFileName);
            // 模板路径
            InputStream tempFileStream = PathUtil.readFileAsStream(templateFileName);
            log.info("tempFileStream=" + tempFileStream.toString());
            // 构造数据
            HashMap<String, Object> values = new HashMap();
            // 构造数据
            values.put("plantName", "1222");

            export.exportDataWord(tempFileStream, null, values);
        } catch (IOException e) {
            log.error("生成word失败，原因：", e);
        }
    }
}

