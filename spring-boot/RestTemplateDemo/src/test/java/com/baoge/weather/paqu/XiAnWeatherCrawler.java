package com.baoge.weather.paqu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiAnWeatherCrawler {

    // 城市编码与名称映射
    private static final List<String> CITY_NOS = Arrays.asList(
            "61401","61402","61403","61404","61405","61406","61407","61408","61409","61410","61411"
    );
    private static final List<String> CITY_NAMES = Arrays.asList(
            "国网西安供电公司","国网渭南供电公司","国网咸阳供电公司","国网宝鸡供电公司",
            "国网汉中供电公司","国网铜川供电公司","国网安康供电公司","国网商洛供电公司",
            "国网延安供电公司","国网榆林供电公司","国网西咸供电公司"
    );

    public static void main(String[] args) {
        Map<String, String> weatherUrlMap = new HashMap<>();
        weatherUrlMap.put("国网西安供电公司", "https://www.lishidata.com/weather/%E9%99%95%E8%A5%BF/%E8%A5%BF%E5%AE%89/101110101.html");
//        weatherUrlMap.put("国网渭南供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%B8%AD%E5%8D%97.html");
//        weatherUrlMap.put("国网咸阳供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%92%B8%E9%98%B3.html");
//        weatherUrlMap.put("国网宝鸡供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%AE%9D%E9%B8%A1.html");
//        weatherUrlMap.put("国网汉中供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%B1%89%E4%B8%AD.html");
//        weatherUrlMap.put("国网铜川供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E9%93%9C%E5%B7%9D.html");
//        weatherUrlMap.put("国网安康供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%AE%89%E5%BA%B7.html");
//        weatherUrlMap.put("国网商洛供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%95%86%E6%B4%9B.html");
//        weatherUrlMap.put("国网延安供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%BB%B6%E5%AE%89.html");
//        weatherUrlMap.put("国网榆林供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%A6%86%E6%9E%97.html");

        try {
            // 发起请求，模拟浏览器
            for (Map.Entry<String, String> entry : weatherUrlMap.entrySet()) {
                String cityName = entry.getKey();
                String cityUrl = entry.getValue();
                Document doc = Jsoup.connect(cityUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(10000)
                        .get();

                // 提取【过去24小时】的第一个表格
                Elements tables = doc.select("table");
                if (tables.isEmpty()) {
                    System.out.println("未找到数据表格");
                    return;
                }
                Element hourTable = tables.get(0); // 第一个table就是24小时数据
                Elements rows = hourTable.select("tr");

                // 逐行解析（跳过表头）
                System.out.println("===== " + cityName + "过去24小时逐小时天气 =====");
                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements tds = row.select("td");

                    if (tds.size() < 12) continue;

                    // 提取关键字段
                    String time = tds.get(1).text().trim();
                    String weather = tds.get(2).text().trim();
                    String temp = tds.get(3).text().trim();
                    String rain = tds.get(4).text().trim();
                    String windDir = tds.get(5).text().trim();
                    String windPower = tds.get(6).text().trim();
                    String windSpeed = tds.get(7).text().trim();
                    String humidity = tds.get(9).text().trim();
                    String shortwaveRadiation = tds.get(13).text().trim();

                    // 输出结果
                    System.out.printf("时间:%s | 天气:%s | 温度:%s | 降水:%s | 风向:%s | 风速:%s | 风力等级:%s | 湿度:%s | 短波辐射:%s%n",
                            time, weather, temp, rain, windDir, windSpeed, windPower, humidity, shortwaveRadiation);
                }
            }
        } catch (IOException e) {
            System.err.println("爬取失败：" + e.getMessage());
        }
    }
}