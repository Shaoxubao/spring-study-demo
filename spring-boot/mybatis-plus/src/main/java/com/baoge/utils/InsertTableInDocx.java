package com.baoge.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 在world文档指定位置,插入表格
 */
public class InsertTableInDocx {
    public static void main(String[] args) {
        String templatePath = "E:/体检卡出库单.docx";
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(templatePath);
            XWPFDocument doc = new XWPFDocument(in);
            // 文本替换
            Map<String, String> param = new HashMap<String, String>();
            param.put("${orderNumber}", "ORD00000001");
            param.put("outWordNumber", "OUW00000001");
            param.put("outNumber", "OUT00000001");
            param.put("outType", "C端商城订单");
            param.put("name", "云龙斯基");
            param.put("distributionForm", "纸质体检卡");
            param.put("wordPerson", "云龙斯基");
            param.put("wordDepartment", "体检部");
            param.put("wordPH", "17352545182");
            param.put("wordEmile", "17352545182@163.com");
            param.put("cardNumber", "100");
            param.put("cardOutName", "云龙斯基");
            param.put("outTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


            List<XWPFParagraph> allXWPFParagraphs = doc.getParagraphs();

            for (XWPFParagraph xwpfParagraph : allXWPFParagraphs) {
                List<XWPFRun> runs = xwpfParagraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null && text != "") {

                        // 指定位置开始创建表格 用able作为标记
                        if (text.equals("able")) {


                            // 自定义生成list测试数据
                            List<Map<String, String>> list = new ArrayList<Map<String, String>>();

                            Map<String, String> map = new HashMap();
                            map.put("number", "1");
                            map.put("cardNumber", "体检卡1");
                            map.put("schemeName", "方案1");
                            map.put("cardYear", "2022");
                            map.put("cardType", "纸质卡");

                            Map<String, String> map2 = new HashMap();
                            map2.put("number", "2");
                            map2.put("cardNumber", "体检卡2");
                            map2.put("schemeName", "方案2");
                            map2.put("cardYear", "2022");
                            map2.put("cardType", "纸质卡");

                            list.add(map);
                            list.add(map2);

                            // 动态集合格式封装成list<map>  这里定义map的key集合
                            List<String> keyList = new ArrayList<String>();
                            keyList.add("number");
                            keyList.add("cardNumber");
                            keyList.add("schemeName");
                            keyList.add("cardYear");
                            keyList.add("cardType");

                            // 表的行数
                            int rows = list.size();


                            // 设置表头集合
                            List<String> textList = new ArrayList<String>();
                            textList.add("序号");
                            textList.add("体检卡号");
                            textList.add("方案名称");
                            textList.add("体检年度");
                            textList.add("体检卡类型");
                            // 表的列数
                            int cols = textList.size();

                            XmlCursor cursor = xwpfParagraph.getCTP().newCursor();
                            XWPFTable tableOne = doc.insertNewTbl(cursor);


                            // 样式控制
                            CTTbl ttbl = tableOne.getCTTbl();
                            CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
                            CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
                            CTJc cTJc = tblPr.addNewJc();
                            cTJc.setVal(STJc.Enum.forString("center")); // 表格居中
                            tblWidth.setW(new BigInteger("9000")); // 每个表格宽度
                            tblWidth.setType(STTblWidth.DXA);


                            // 表格的表头创建,去上边textList表头集合的字段
                            XWPFTableRow tableRowTitle = tableOne.getRow(0);
                            tableRowTitle.getCell(0).setText(textList.get(0));
                            tableRowTitle.addNewTableCell().setText(textList.get(1));
                            tableRowTitle.addNewTableCell().setText(textList.get(2));
                            tableRowTitle.addNewTableCell().setText(textList.get(3));
                            tableRowTitle.addNewTableCell().setText(textList.get(4));

                            for (Map<String, String> cardMap : list) {
                                // 遍历list 得到要导出的每一行表格数据,数据结构是map
                                XWPFTableRow createRow = tableOne.createRow();

                                for (int i = 0; i < keyList.size(); i++) {
                                    createRow.getCell(i).setText(cardMap.get(keyList.get(i)));
                                }
                            }
                            run.setText("", 0);
                        } else {
                            for (Entry<String, String> entry : param.entrySet()) {
                                String key = entry.getKey();
                                int i = text.indexOf(key);
                                if (text.indexOf(key) != -1) {
                                    text = text.replace(key, entry.getValue());
                                    run.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }

            out = new FileOutputStream("E:/体检卡出库单-new.docx");
            // 输出
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}