package com.baoge.utils;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class ExportWordWithNewTemplate {

    private  static String[] points = new String[] {
            "00:15","00:30","00:45","01:00","01:15","01:30","01:45","02:00","02:15","02:30","02:45",
            "03:00","03:15","03:30","03:45","04:00","04:15","04:30","04:45","05:00","05:15","05:30",
            "05:45","06:00","06:15","06:30","06:45","07:00","07:15","07:30","07:45","08:00","08:15",
            "08:30","08:45","09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45","11:00",
            "11:15","11:30","11:45","12:00","12:15","12:30","12:45","13:00","13:15","13:30","13:45",
            "14:00","14:15","14:30","14:45","15:00","15:15","15:30","15:45","16:00","16:15","16:30",
            "16:45","17:00","17:15","17:30","17:45","18:00","18:15","18:30","18:45","19:00","19:15",
            "19:30","19:45","20:00","20:15","20:30","20:45","21:00","21:15","21:30","21:45","22:00",
            "22:15","22:30","22:45","23:00", "23:15","23:30", "23:45", "00:00"};

    /**
     * 导出word
     * @throws IOException
     */
    public void exportDataWord(InputStream inputStream, HttpServletResponse response, Map<String, Object> renderData) throws Exception {
        try {
            log.info("虚拟电厂能力测试报告exportDataWord");
            //渲染表格  动态行列
            LoopRowTableRenderPolicy rowPolicy = new LoopRowTableRenderPolicy();
            LoopColumnTableRenderPolicy colPolicy = new LoopColumnTableRenderPolicy();
            Configure config = Configure.builder()
//                    .bind("consList", rowPolicy)
//                    .bind("stdataList", colPolicy)
//                    .bind("xtdataList", colPolicy)
//                    .bind("attachment", new AttachmentRenderPolicy())
//                    .bind("xlsx", new AttachmentRenderPolicy())
                    .build();

            XWPFTemplate template = XWPFTemplate.compile(inputStream,config).render(renderData);
            log.info("虚拟电厂能力测试报告XWPFTemplate.compile完成");
            //=================生成word到设置浏览默认下载地址=================
            // 设置强制下载不打开
//            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//            response.setCharacterEncoding("utf-8");
            // 设置文件名
            String  name =renderData.get("plantName") + ".docx";
            String docName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
//            response.addHeader("Content-Disposition", "attachment;filename=" + docName);
            log.info("虚拟电厂能力测试报告fileName="+docName);
//            OutputStream out = response.getOutputStream();
            File f = new File(name);
            OutputStream out = new FileOutputStream(f);
            template.write(out);
            out.flush();
            out.close();
            template.close();
        } catch (Exception e) {
            log.error("生成Word失败，原因：", e);
            throw new Exception();
        }
    }

        public static void main(String[] args) {
            ExportWordWithNewTemplate export = new ExportWordWithNewTemplate();
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

            List<Map<String, Object>> curves = new ArrayList<>();
            Map<String, Object> stCurve = new HashMap<String, Object>() {
                {
                    put("chart", Charts.ofComboSeries("上调负荷曲线", points)
//                            .addLineSeries("实时负荷", (Number[]) buildData().toArray(new Double[0]))
//                            .addLineSeries("基线负荷", (Number[]) buildData().toArray(new Double[0]))
                            .addLineSeries("上报负荷", (Number[]) buildData().toArray(new Double[0]))
                            .addAreaSeries("调节区间", (Number[]) buildData2().toArray(new Double[0]))  // 面积图
                            .create());
                    put("chartTitle","图1 上调负荷曲线");
                }
            };
            curves.add(stCurve);
            values.put("curves", curves);

            export.exportDataWord(tempFileStream, null, values);
        } catch (Exception e) {
            log.error("生成word失败，原因：", e);
        }
    }

    private static List<Double> buildData() {
        List<Double> randomList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 96; i++) {
            // 生成 0.0（包含）到 100.0（不包含）的随机数
            double randomValue = random.nextDouble() * 100;
            randomList.add(randomValue);
        }
        return randomList;
    }

    private static List<Double> buildData2() {
        List<Double> randomList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 96; i++) {
            randomList.add(null);
        }

        for (int i = 20; i < 25; i++) {
            // 生成 0.0（包含）到 100.0（不包含）的随机数
            double randomValue = random.nextDouble() * 100;
            randomList.set(i, randomValue);
        }

        System.out.println(randomList);
        return randomList;
    }
}

