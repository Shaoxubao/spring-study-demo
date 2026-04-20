package com.baoge.weather;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WeatherExcelImporter {
    private static final Logger log = LoggerFactory.getLogger(WeatherExcelImporter.class);

    // 城市编码与名称映射
    private static final List<String> CITY_NOS = Arrays.asList(
            "61401","61402","61403","61404","61405","61406","61407","61408","61409","61410","61411"
    );
    private static final List<String> CITY_NAMES = Arrays.asList(
            "国网西安供电公司","国网渭南供电公司","国网咸阳供电公司","国网宝鸡供电公司",
            "国网汉中供电公司","国网铜川供电公司","国网安康供电公司","国网商洛供电公司",
            "国网延安供电公司","国网榆林供电公司","国网西咸供电公司"
    );
    private static final Map<String, String> CITY_NAME_TO_NO_MAP = new HashMap<>();
    static {
        for (int i = 0; i < CITY_NAMES.size(); i++) {
            CITY_NAME_TO_NO_MAP.put(CITY_NAMES.get(i), CITY_NOS.get(i));
        }
    }

    // 天气编码映射
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

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String INSERT_SQL = "INSERT IGNORE INTO weather_data_hour " +
            "(time, city_no, city_name, load_mw, temp, humidity, weather_code, is_rain, hour, day_of_week, is_holiday, " +
            "shortwave_radiation, direct_radiation, diffuse_radiation, sunshine_duration, wind_level, wind_direction, wind_gusts, wind_speed) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private String excelDataPath;

    public WeatherExcelImporter(String excelDataPath) {
        this.excelDataPath = excelDataPath;
    }

    public void importAll() {
        File dir = new File(excelDataPath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.error("Excel 数据目录不存在：{}", excelDataPath);
            return;
        }
        File[] files = dir.listFiles((d, n) -> n.toLowerCase().endsWith(".xlsx"));
        if (files == null || files.length == 0) {
            log.info("文件夹下无 .xlsx 文件");
            return;
        }
        for (File file : files) {
            try {
                processFile(file);
            } catch (Exception e) {
                log.error("处理文件 {} 失败", file.getName(), e);
            }
        }
    }

    private void processFile(File file) throws Exception {
        log.info("开始处理文件：{}", file.getName());
        try (InputStream is = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(is);
             Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 跳过表头
                Row row = sheet.getRow(i);
                if (row == null) continue;
                WeatherDataHour data = parseRow(row);
                if (data == null) continue;
                setPreparedStatement(ps, data);
                ps.addBatch();
                count++;
                // 每 1000 条提交一次
                if (count % 1000 == 0) {
                    ps.executeBatch();
                    conn.commit();
                    log.info("已批量写入 {} 条数据", count);
                }
            }
            ps.executeBatch();
            conn.commit();
            log.info("文件 {} 处理完成，共写入 {} 条数据", file.getName(), count);
            ps.close();
        }
    }

    private WeatherDataHour parseRow(Row row) {
        try {
            // 1. 地区：陕西/咸阳/三原 → 国网咸阳供电公司
            String region = getCellValue(row.getCell(0));
            String cityName = parseCityName(region);
            if (cityName == null) return null;
            String cityNo = CITY_NAME_TO_NO_MAP.get(cityName);
            if (cityNo == null) return null;

            // 2. 时间
            String timeStr = getCellValue(row.getCell(1));
            if (StringUtils.isBlank(timeStr)) return null;
            LocalDateTime time;
            if (timeStr.length() <= 10) {
                time = LocalDateTime.parse(timeStr + " 00:00:00", TIME_FORMATTER);
            } else {
                timeStr = timeStr + ":00";
                time = LocalDateTime.parse(timeStr, TIME_FORMATTER);
            }
            int hour = time.getHour();
            DayOfWeek dayOfWeekEnum = time.getDayOfWeek();
            int dayOfWeek = dayOfWeekEnum.getValue();
            int isHoliday = HolidayUtil.getHolidayFlag(time.toLocalDate());

            // 3. 天气相关
            String weather = getCellValue(row.getCell(2));
            Integer weatherCode = WEATHER_CODE_MAP.getOrDefault(weather, 99);
            BigDecimal temp = getDecimalCell(row.getCell(3));
            BigDecimal precipitation = getDecimalCell(row.getCell(4));
            Integer isRain = (precipitation != null && precipitation.compareTo(BigDecimal.ZERO) > 0) ? 1 : 0;
            Integer windLevel = getIntegerCell(row.getCell(6));
            BigDecimal windDirection = getDecimalCell(row.getCell(8));
            BigDecimal windSpeedKm = getDecimalCell(row.getCell(7));
            BigDecimal windSpeed = null;
//            if (windSpeedKm != null) {
//                windSpeed = windSpeedKm.multiply(new BigDecimal("0.2778")).setScale(1, BigDecimal.ROUND_HALF_UP);
//            }
            Integer humidity = getIntegerCell(row.getCell(10));

            // 4. 辐射数据
            BigDecimal shortwaveRadiation = getDecimalCell(row.getCell(15)); // 短波辐射（P列）
            BigDecimal directRadiation = getDecimalCell(row.getCell(16));    // 直射辐射（Q列）
            BigDecimal diffuseRadiation = getDecimalCell(row.getCell(17));   // 散射辐射（R列）

            // 日照时长（示例：按直射辐射估算）
            Integer sunshineDuration = null;
            if (directRadiation != null && directRadiation.compareTo(BigDecimal.ZERO) > 0) {
                sunshineDuration = (int) Math.min(3600, directRadiation.multiply(new BigDecimal("10")).longValue());
            }

            WeatherDataHour data = new WeatherDataHour();
            data.setTime(time);
            data.setCityNo(cityNo);
            data.setCityName(cityName);
            data.setTemp(temp);
            data.setHumidity(humidity);
            data.setWeatherCode(weatherCode);
            data.setIsRain(isRain);
            data.setHour(hour);
            data.setDayOfWeek(dayOfWeek);
            data.setIsHoliday(isHoliday);
            data.setShortwaveRadiation(shortwaveRadiation);
            data.setDirectRadiation(directRadiation);
            data.setDiffuseRadiation(diffuseRadiation);
            data.setSunshineDuration(sunshineDuration);
            data.setWindLevel(windLevel);
            data.setWindDirection(windDirection);
            data.setWindSpeed(windSpeedKm);
            data.setLoadMw(null); // Excel中无负荷数据
            data.setWindGusts(null); // Excel中无阵风数据
            return data;
        } catch (Exception e) {
            log.warn("解析行数据失败，跳过该行", e);
            return null;
        }
    }

    private void setPreparedStatement(PreparedStatement ps, WeatherDataHour data) throws Exception {
        ps.setObject(1, data.getTime());
        ps.setString(2, data.getCityNo());
        ps.setString(3, data.getCityName());
        ps.setBigDecimal(4, data.getLoadMw());
        ps.setBigDecimal(5, data.getTemp());
        ps.setObject(6, data.getHumidity());
        ps.setObject(7, data.getWeatherCode());
        ps.setObject(8, data.getIsRain());
        ps.setObject(9, data.getHour());
        ps.setObject(10, data.getDayOfWeek());
        ps.setObject(11, data.getIsHoliday());
        ps.setBigDecimal(12, data.getShortwaveRadiation());
        ps.setBigDecimal(13, data.getDirectRadiation());
        ps.setBigDecimal(14, data.getDiffuseRadiation());
        ps.setObject(15, data.getSunshineDuration());
        ps.setObject(16, data.getWindLevel());
        ps.setBigDecimal(17, data.getWindDirection());
        ps.setBigDecimal(18, data.getWindGusts());
        ps.setBigDecimal(19, data.getWindSpeed());
    }

    private String parseCityName(String region) {
        if (StringUtils.isBlank(region)) return null;
        String[] parts = region.split("/");
        if (parts.length < 2) return null;
        String cityPart = parts[1];
        for (String name : CITY_NAMES) {
            if (name.contains(cityPart)) {
                return name;
            }
        }
        return null;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getDecimalCell(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        String str = getCellValue(cell);
        if (StringUtils.isBlank(str)) return null;
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getIntegerCell(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        String str = getCellValue(cell);
        if (StringUtils.isBlank(str)) return null;
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }
}