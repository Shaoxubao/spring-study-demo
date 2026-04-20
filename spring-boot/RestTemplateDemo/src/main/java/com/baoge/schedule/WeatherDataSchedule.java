package com.baoge.schedule;

import com.baoge.weather.HolidayUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherDataSchedule {

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataSchedule.class);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, Integer> WEATHER_CODE_MAP = new HashMap<>();
    static {
        WEATHER_CODE_MAP.put("晴", 0);
        WEATHER_CODE_MAP.put("多云", 1);
        WEATHER_CODE_MAP.put("阴", 2);
        WEATHER_CODE_MAP.put("小雨", 3);
        WEATHER_CODE_MAP.put("中雨", 4);
        WEATHER_CODE_MAP.put("霾", 5);
        WEATHER_CODE_MAP.put("浮尘", 6);
    }

    private static final Map<String, Integer> WIND_LEVEL_MAP = new HashMap<>();
    static {
        WIND_LEVEL_MAP.put("无风", 0);
        WIND_LEVEL_MAP.put("软风", 1);
        WIND_LEVEL_MAP.put("轻风", 2);
        WIND_LEVEL_MAP.put("微风", 3);
        WIND_LEVEL_MAP.put("和风", 4);
        WIND_LEVEL_MAP.put("清风", 5);
        WIND_LEVEL_MAP.put("强风", 6);
        WIND_LEVEL_MAP.put("疾风", 7);
        WIND_LEVEL_MAP.put("大风", 8);
        WIND_LEVEL_MAP.put("烈风", 9);
        WIND_LEVEL_MAP.put("狂风", 10);
        WIND_LEVEL_MAP.put("暴风", 11);
        WIND_LEVEL_MAP.put("飓风", 12);
    }

    private static final Map<String, BigDecimal> WIND_DIRECTION_MAP = new HashMap<>();
    static {
        WIND_DIRECTION_MAP.put("北", new BigDecimal("0"));
        WIND_DIRECTION_MAP.put("东北", new BigDecimal("45"));
        WIND_DIRECTION_MAP.put("东", new BigDecimal("90"));
        WIND_DIRECTION_MAP.put("东南", new BigDecimal("135"));
        WIND_DIRECTION_MAP.put("南", new BigDecimal("180"));
        WIND_DIRECTION_MAP.put("西南", new BigDecimal("225"));
        WIND_DIRECTION_MAP.put("西", new BigDecimal("270"));
        WIND_DIRECTION_MAP.put("西北", new BigDecimal("315"));
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/load_forecasting?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void insertWeatherData(Connection conn, LocalDateTime time, String cityNo, String cityName,
                                          BigDecimal temp, Integer humidity, Integer weatherCode, Integer isRain,
                                          Integer hour, Integer dayOfWeek, Integer isHoliday,
                                          BigDecimal shortwaveRadiation, BigDecimal directRadiation,
                                          BigDecimal diffuseRadiation, Integer sunshineDuration,
                                          Integer windLevel, BigDecimal windDirection, BigDecimal windGusts,
                                          BigDecimal windSpeed) throws SQLException {
        String sql = "INSERT INTO weather_data (time, city_no, city_name, temp, humidity, weather_code, is_rain, " +
                "hour, day_of_week, is_holiday, shortwave_radiation, direct_radiation, diffuse_radiation, " +
                "sunshine_duration, wind_level, wind_direction, wind_gusts, wind_speed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "temp = VALUES(temp), humidity = VALUES(humidity), weather_code = VALUES(weather_code), " +
                "is_rain = VALUES(is_rain), day_of_week = VALUES(day_of_week), is_holiday = VALUES(is_holiday), " +
                "shortwave_radiation = VALUES(shortwave_radiation), direct_radiation = VALUES(direct_radiation), " +
                "diffuse_radiation = VALUES(diffuse_radiation), sunshine_duration = VALUES(sunshine_duration), " +
                "wind_level = VALUES(wind_level), wind_direction = VALUES(wind_direction), " +
                "wind_gusts = VALUES(wind_gusts), wind_speed = VALUES(wind_speed)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(time));
            pstmt.setString(2, cityNo);
            pstmt.setString(3, cityName);
            pstmt.setBigDecimal(4, temp);
            pstmt.setInt(5, humidity != null ? humidity : 0);
            pstmt.setInt(6, weatherCode != null ? weatherCode : 0);
            pstmt.setInt(7, isRain != null ? isRain : 0);
            pstmt.setInt(8, hour != null ? hour : 0);
            pstmt.setInt(9, dayOfWeek != null ? dayOfWeek : 0);
            pstmt.setInt(10, isHoliday != null ? isHoliday : 0);
            pstmt.setBigDecimal(11, shortwaveRadiation);
            pstmt.setBigDecimal(12, directRadiation);
            pstmt.setBigDecimal(13, diffuseRadiation);
            pstmt.setObject(14, sunshineDuration, Types.INTEGER);
            pstmt.setObject(15, windLevel, Types.INTEGER);
            pstmt.setBigDecimal(16, windDirection);
            pstmt.setBigDecimal(17, windGusts);
            pstmt.setBigDecimal(18, windSpeed);
            pstmt.executeUpdate();
        }
    }

    private static Integer parseWindLevel(String windPower) {
        if (windPower == null || windPower.isEmpty()) return null;
        try {
            String levelStr = windPower.replaceAll("[^0-9]", "");
            if (!levelStr.isEmpty()) {
                return Integer.parseInt(levelStr);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    private static BigDecimal parseWindDirection(String windDir) {
        if (windDir == null || windDir.isEmpty()) return null;
        String dirStr = windDir.replace("风", "").trim();
        for (Map.Entry<String, BigDecimal> entry : WIND_DIRECTION_MAP.entrySet()) {
            if (dirStr.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static Integer parseWeatherCode(String weather) {
        if (weather == null || weather.isEmpty()) return null;
        for (Map.Entry<String, Integer> entry : WEATHER_CODE_MAP.entrySet()) {
            if (weather.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static Integer parseIsRain(String rain) {
        if (rain == null || rain.isEmpty()) return 0;
        try {
            String rainStr = rain.replaceAll("[^0-9.]", "");
            if (!rainStr.isEmpty()) {
                double rainValue = Double.parseDouble(rainStr);
                return rainValue > 0 ? 1 : 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    private static BigDecimal parseDecimal(String value) {
        if (value == null || value.isEmpty() || "-".equals(value)) return null;
        try {
            String numStr = value.replaceAll("[^0-9.]", "");
            if (!numStr.isEmpty()) {
                return new BigDecimal(numStr);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isEmpty() || "-".equals(value)) return null;
        try {
            String numStr = value.replaceAll("[^0-9]", "");
            if (!numStr.isEmpty()) {
                return Integer.parseInt(numStr);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    private static LocalDateTime parseTime(String timeStr) {
        try {
            if (timeStr.contains("今天")) {
                return LocalDateTime.now().withHour(Integer.parseInt(timeStr.replaceAll("[^0-9]", ""))).withMinute(0).withSecond(0).withNano(0);
            } else if (timeStr.contains("昨天")) {
                return LocalDateTime.now().minusDays(1).withHour(Integer.parseInt(timeStr.replaceAll("[^0-9]", ""))).withMinute(0).withSecond(0).withNano(0);
            } else if (timeStr.contains("前天")) {
                return LocalDateTime.now().minusDays(2).withHour(Integer.parseInt(timeStr.replaceAll("[^0-9]", ""))).withMinute(0).withSecond(0).withNano(0);
            }

            String[] parts = timeStr.split(" ");
            if (parts.length == 2) {
                String datePart = parts[0];
                String timePart = parts[1];

                int currentYear = LocalDateTime.now().getYear();
                String fullDateStr = currentYear + "-" + datePart + " " + timePart + ":00";
                return LocalDateTime.parse(fullDateStr, TIME_FORMATTER);
            }
            return LocalDateTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getCityNo(String cityName) {
        Map<String, String> cityMap = new HashMap<>();
        cityMap.put("国网西安供电公司", "61401");
        cityMap.put("国网渭南供电公司", "61402");
        cityMap.put("国网咸阳供电公司", "61403");
        cityMap.put("国网宝鸡供电公司", "61404");
        cityMap.put("国网汉中供电公司", "61405");
        cityMap.put("国网铜川供电公司", "61406");
        cityMap.put("国网安康供电公司", "61407");
        cityMap.put("国网商洛供电公司", "61408");
        cityMap.put("国网延安供电公司", "61409");
        cityMap.put("国网榆林供电公司", "61410");
        cityMap.put("国网西咸供电公司", "61411");
        return cityMap.get(cityName);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void crawlWeatherData() {
        logger.info("开始执行天气数据爬取定时任务");

        Map<String, String> weatherUrlMap = new HashMap<>();
        weatherUrlMap.put("国网西安供电公司", "https://www.lishidata.com/weather/%E9%99%95%E8%A5%BF/%E8%A5%BF%E5%AE%89/101110101.html");
        weatherUrlMap.put("国网渭南供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%B8%AD%E5%8D%97.html");
        weatherUrlMap.put("国网咸阳供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%92%B8%E9%98%B3.html");
        weatherUrlMap.put("国网宝鸡供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%AE%9D%E9%B8%A1.html");
        weatherUrlMap.put("国网汉中供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%B1%89%E4%B8%AD.html");
        weatherUrlMap.put("国网铜川供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E9%93%9C%E5%B7%9D.html");
        weatherUrlMap.put("国网安康供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%AE%89%E5%BA%B7.html");
        weatherUrlMap.put("国网商洛供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%95%86%E6%B4%9B.html");
        weatherUrlMap.put("国网延安供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E5%BB%B6%E5%AE%89.html");
        weatherUrlMap.put("国网榆林供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E6%A6%86%E6%9E%97.html");
        weatherUrlMap.put("国网西咸供电公司", "https://www.lishidata.com/area/%E9%99%95%E8%A5%BF/%E8%A5%BF%E5%92%B8.html");

        try (Connection conn = getConnection()) {
            for (Map.Entry<String, String> entry : weatherUrlMap.entrySet()) {
                String cityName = entry.getKey();
                String cityUrl = entry.getValue();
                String cityNo = getCityNo(cityName);

                logger.info("开始爬取: {}", cityName);
                Document doc = Jsoup.connect(cityUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(10000)
                        .get();

                Elements tables = doc.select("table");
                if (tables.isEmpty()) {
                    logger.warn("未找到数据表格");
                    continue;
                }
                Element hourTable = tables.get(0);
                Elements rows = hourTable.select("tr");

                int successCount = 0;
                int failCount = 0;
                logger.info("表格总行数: {}", rows.size());

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements tds = row.select("td");

                    logger.debug("第{}行，列数: {}", i, tds.size());

                    if (tds.size() < 10) {
                        logger.debug("跳过：列数不足");
                        failCount++;
                        continue;
                    }

                    String timeStr = tds.get(1).text().trim();
                    String weather = tds.get(2).text().trim();
                    String temp = tds.get(3).text().trim();
                    String rain = tds.get(4).text().trim();
                    String windDir = tds.get(5).text().trim();
                    String windSpeed = tds.get(7).text().trim();
                    String windPower = tds.get(6).text().trim();
                    String humidity = tds.get(9).text().trim();
                    String shortwaveRadiation = tds.get(13).text().trim();

                    logger.debug("原始数据 - 时间:{} 天气:{} 温度:{} 降水:{} 风向:{} 风速:{} 风力:{} 湿度:{} 辐射:{}",
                            timeStr, weather, temp, rain, windDir, windSpeed, windPower, humidity, shortwaveRadiation);

                    LocalDateTime time = parseTime(timeStr);
                    if (time == null) {
                        logger.debug("跳过：时间解析失败 - {}", timeStr);
                        failCount++;
                        continue;
                    }
                    logger.debug("解析后时间: {}", time);

                    BigDecimal tempValue = parseDecimal(temp);
                    Integer humidityValue = parseInteger(humidity);
                    Integer weatherCode = parseWeatherCode(weather);
                    Integer isRain = parseIsRain(rain);
                    Integer hour = time.getHour();
                    Integer dayOfWeek = time.getDayOfWeek().getValue();
                    Integer isHoliday = HolidayUtil.getHolidayFlag(time.toLocalDate());
                    BigDecimal shortwaveRadiationValue = parseDecimal(shortwaveRadiation);
                    BigDecimal directRadiation = null;
                    BigDecimal diffuseRadiation = null;
                    Integer sunshineDuration = null;
                    Integer windLevel = parseWindLevel(windPower);
                    BigDecimal windDirection = parseWindDirection(windDir);
                    BigDecimal windGusts = parseDecimal(windSpeed);
                    BigDecimal windSpeedValue = parseDecimal(windSpeed);

                    logger.debug("解析后数据 - 温度:{} 湿度:{} 天气编码:{} 是否降雨:{} 风力等级:{} 风向:{} 风速:{} 辐射:{}",
                            tempValue, humidityValue, weatherCode, isRain, windLevel, windDirection, windSpeedValue, shortwaveRadiationValue);

                    try {
                        insertWeatherData(conn, time, cityNo, cityName, tempValue, humidityValue, weatherCode,
                                isRain, hour, dayOfWeek, isHoliday, shortwaveRadiationValue, directRadiation,
                                diffuseRadiation, sunshineDuration, windLevel, windDirection, windGusts, windSpeedValue);
                        successCount++;
                        logger.debug("插入成功");
                    } catch (SQLException e) {
                        logger.error("插入数据失败: {}", e.getMessage());
                        failCount++;
                    } catch (Exception e) {
                        logger.error("插入数据时发生异常: {}", e.getMessage());
                        failCount++;
                    }
                }
                logger.info("{} 成功插入 {} 条数据，失败 {} 条", cityName, successCount, failCount);
            }
            logger.info("所有数据爬取完成");
        } catch (SQLException e) {
            logger.error("数据库连接失败: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("爬取失败: {}", e.getMessage());
        }
    }
}