package com.baoge.weather.hefeng;

import com.baoge.weather.HolidayUtil;
import com.baoge.weather.WindLevelUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class HeFengWeatherToMySQL {

    // ================== 请填写你的信息 ==================
    private static final String QF_KEY = "你的和风天气KEY";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/load_forecasting?useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "123456";
    // ===================================================

    private static final List<String> CITY_NOS = Arrays.asList(
            "61401","61402","61403","61404","61405","61406","61407","61408","61409","61410","61411"
    );

    private static final List<String> CITY_NAMES = Arrays.asList(
            "国网西安供电公司","国网渭南供电公司","国网咸阳供电公司","国网宝鸡供电公司",
            "国网汉中供电公司","国网铜川供电公司","国网安康供电公司","国网商洛供电公司",
            "国网延安供电公司","国网榆林供电公司","国网西咸供电公司"
    );

    // 和风城市ID
    private static final List<String> HEFENG_CITY_IDS = Arrays.asList(
            "101110101","101110301","101110201","101110801","101111401",
            "101110601","101111501","101111301","101111201","101111001","101110114"
    );

    private static final OkHttpClient client = new OkHttpClient();
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void main(String[] args) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(3);

        for (int i = 0; i < CITY_NOS.size(); i++) {
            String cityNo = CITY_NOS.get(i);
            String cityName = CITY_NAMES.get(i);
            String cityId = HEFENG_CITY_IDS.get(i);
            System.out.println("===== 拉取：" + cityName + " =====");
            try {
                fetchHistory(cityNo, cityName, cityId, end, end);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("✅ 全部完成！");
    }

    private static void fetchHistory(String cityNo, String cityName, String cityId, LocalDate start, LocalDate end) throws Exception {
        LocalDate date = start;
        while (!date.isAfter(end)) {
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String url = "https://devapi.qweather.com/v7/historical/weather?location=" + cityId + "&date=" + dateStr + "&key=" + QF_KEY;

            Request request = new Request.Builder().url(url).build();
            try (Response res = client.newCall(request).execute()) {
                if (!res.isSuccessful()) continue;
                JsonObject root = JsonParser.parseString(res.body().string()).getAsJsonObject();
                if (!"200".equals(root.get("code").getAsString())) continue;

                JsonArray hourList = root.getAsJsonArray("hourly");
                for (int j = 0; j < hourList.size(); j++) {
                    JsonObject hour = hourList.get(j).getAsJsonObject();

                    String time = hour.get("fxTime").getAsString();
                    LocalDateTime dt = LocalDateTime.parse(time, DTF);

                    double temp = hour.get("temp").getAsDouble();
                    int humidity = hour.get("humidity").getAsInt();
                    int weatherCode = hour.get("icon").getAsInt();
                    double precip = hour.get("precip").getAsDouble();
                    double windSpeed = hour.get("windSpeed").getAsDouble();
                    double windDir = hour.get("windDirection").getAsDouble();
                    double windGusts = 0;
                    if (hour.has("windGust") && !hour.get("windGust").isJsonNull()) {
                        windGusts = hour.get("windGust").getAsDouble();
                    }
                    int windLevel = WindLevelUtil.getWindLevel(windSpeed);
                    int isRain = precip > 0 ? 1 : 0;
                    int hourVal = dt.getHour();
                    int dayOfWeek = dt.getDayOfWeek().getValue();
                    int isHoliday = HolidayUtil.getHolidayFlag(dt.toLocalDate());

                    insert(cityNo, cityName, dt, temp, humidity, weatherCode, isRain,
                            windSpeed, windDir, windGusts, windLevel,
                            hourVal, dayOfWeek, isHoliday);
                }
                System.out.println("完成日期：" + dateStr);
            }
            date = date.plusDays(1);
            Thread.sleep(200);
        }
    }

    private static void insert(String cityNo, String cityName, LocalDateTime dt,
                               double temp, int humidity, int weatherCode, int isRain,
                               double windSpeed, double windDir, double windGusts, int windLevel,
                               int hour, int dayOfWeek, int isHoliday) throws SQLException {

        String sql = "INSERT INTO weather_data " +
                "(city_no,city_name,time,temp,humidity,weather_code,is_rain," +
                "wind_speed,wind_direction,wind_gusts,wind_level," +
                "hour,day_of_week,is_holiday) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "temp=VALUES(temp),humidity=VALUES(humidity),weather_code=VALUES(weather_code)," +
                "is_rain=VALUES(is_rain),wind_speed=VALUES(wind_speed)," +
                "wind_direction=VALUES(wind_direction),wind_gusts=VALUES(wind_gusts),wind_level=VALUES(wind_level)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cityNo);
            pstmt.setString(2, cityName);
            pstmt.setString(3, dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")));
            pstmt.setDouble(4, temp);
            pstmt.setInt(5, humidity);
            pstmt.setInt(6, weatherCode);
            pstmt.setInt(7, isRain);
            pstmt.setDouble(8, windSpeed);
            pstmt.setDouble(9, windDir);
            pstmt.setDouble(10, windGusts);
            pstmt.setInt(11, windLevel);
            pstmt.setInt(12, hour);
            pstmt.setInt(13, dayOfWeek);
            pstmt.setInt(14, isHoliday);
            pstmt.executeUpdate();
        }
    }
}