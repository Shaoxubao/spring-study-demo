package com.baoge.weather;

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

public class OpenMeteoWindGustFix {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/load_forecasting?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "123456";

    private static final List<String> CITY_NOS = Arrays.asList(
            "61401","61402","61403","61404","61405","61406","61407","61408","61409","61410","61411"
    );
    private static final List<String> CITY_NAMES = Arrays.asList(
            "国网西安供电公司","国网渭南供电公司","国网咸阳供电公司","国网宝鸡供电公司",
            "国网汉中供电公司","国网铜川供电公司","国网安康供电公司","国网商洛供电公司",
            "国网延安供电公司","国网榆林供电公司","国网西咸供电公司"
    );
    private static final double[][] CITY_LOC = {
            {34.2646,108.9497}, {34.4967,109.4920}, {34.3373,108.7123}, {34.3640,107.2370},
            {33.0720,107.0200}, {35.0903,109.1146}, {32.6897,109.0275}, {33.8677,109.9377},
            {36.5962,109.4865}, {38.2930,109.7370}, {34.3100,108.8400}
    };

    private static final OkHttpClient client = new OkHttpClient();
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void main(String[] args) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3);

        for (int i = 0; i < CITY_NOS.size(); i++) {
            String cityNo = CITY_NOS.get(i);
            String cityName = CITY_NAMES.get(i);
            double lat = CITY_LOC[i][0];
            double lon = CITY_LOC[i][1];

            System.out.println("\n===== 拉取：" + cityName + " =====");
            try {
                fetchAndSave(cityNo, cityName, lat, lon, startDate, endDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void fetchAndSave(String cityNo, String cityName, double lat, double lon, LocalDate start, LocalDate end) throws Exception {
        String url = "https://archive-api.open-meteo.com/v1/archive" +
                "?latitude=" + lat +
                "&longitude=" + lon +
                "&start_date=" + end +
                "&end_date=" + end +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,precipitation," +
                "wind_speed_10m,wind_direction_10m,wind_gusts_10m," +
                "shortwave_radiation,direct_radiation,diffuse_radiation,sunshine_duration" +
                "&timezone=Asia/Shanghai";

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return;

            JsonObject root = new JsonParser().parse(response.body().string()).getAsJsonObject();
            JsonObject hourly = root.getAsJsonObject("hourly");

            JsonArray timeArr = hourly.getAsJsonArray("time");
            JsonArray tempArr = hourly.getAsJsonArray("temperature_2m");
            JsonArray humidityArr = hourly.getAsJsonArray("relativehumidity_2m");
            JsonArray weatherCodeArr = hourly.getAsJsonArray("weathercode");
            JsonArray precipArr = hourly.getAsJsonArray("precipitation");
            JsonArray windSpeedArr = hourly.getAsJsonArray("wind_speed_10m");
            JsonArray windDirArr = hourly.getAsJsonArray("wind_direction_10m");
            JsonArray windGustsArr = hourly.getAsJsonArray("wind_gusts_10m");
            JsonArray shortRadArr = hourly.getAsJsonArray("shortwave_radiation");
            JsonArray directRadArr = hourly.getAsJsonArray("direct_radiation");
            JsonArray diffuseRadArr = hourly.getAsJsonArray("diffuse_radiation");
            JsonArray sunshineArr = hourly.getAsJsonArray("sunshine_duration");

            batchInsert(cityNo, cityName,
                    timeArr, tempArr, humidityArr, weatherCodeArr, precipArr,
                    windSpeedArr, windDirArr, windGustsArr,
                    shortRadArr, directRadArr, diffuseRadArr, sunshineArr);
        }
    }

    private static void batchInsert(
            String cityNo, String cityName,
            JsonArray timeArr, JsonArray tempArr, JsonArray humidityArr, JsonArray weatherCodeArr, JsonArray precipArr,
            JsonArray windSpeedArr, JsonArray windDirArr, JsonArray windGustsArr,
            JsonArray shortRadArr, JsonArray directRadArr, JsonArray diffuseRadArr, JsonArray sunshineArr
    ) throws SQLException {

        String sql = "INSERT INTO weather_data (" +
                "city_no,city_name,time,temp,humidity,weather_code,is_rain," +
                "wind_speed,wind_direction,wind_gusts,wind_level," +
                "hour,day_of_week,is_holiday," +
                "shortwave_radiation,direct_radiation,diffuse_radiation,sunshine_duration) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "temp=VALUES(temp),humidity=VALUES(humidity),weather_code=VALUES(weather_code)," +
                "is_rain=VALUES(is_rain),wind_speed=VALUES(wind_speed)," +
                "wind_direction=VALUES(wind_direction),wind_gusts=VALUES(wind_gusts),wind_level=VALUES(wind_level)," +
                "shortwave_radiation=VALUES(shortwave_radiation),direct_radiation=VALUES(direct_radiation)," +
                "diffuse_radiation=VALUES(diffuse_radiation),sunshine_duration=VALUES(sunshine_duration)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            int total = timeArr.size();

            for (int i = 0; i < total; i++) {
                LocalDateTime dateTime = LocalDateTime.parse(timeArr.get(i).getAsString(), DTF);

                double temp = tempArr.get(i).getAsDouble();
                int humidity = humidityArr.get(i).getAsInt();
                int weatherCode = weatherCodeArr.get(i).getAsInt();
                double precip = precipArr.get(i).getAsDouble();

                // 风力数据处理（关键修正：阵风单位转换）
                double windSpeed = windSpeedArr.get(i).getAsDouble(); // 单位：m/s
                double windDir = windDirArr.get(i).isJsonNull() ? 0 : windDirArr.get(i).getAsDouble();
                // 【关键修正】阵风单位转换：km/h → m/s
                double windGustsKmh = windGustsArr.get(i).isJsonNull() ? 0 : windGustsArr.get(i).getAsDouble();
                double windGusts = windGustsKmh / 3.6;

                // 计算风力等级（使用m/s的风速）
                // 在入库前，加入修正系数
                double windSpeedRaw = windSpeedArr.get(i).getAsDouble();
                double windSpeedNew = windSpeedRaw * 0.35; // 假设修正系数为0.35，可根据实测对比调整
                int windLevel = WindLevelUtil.getWindLevel(windSpeedNew);

                // 其他气象数据
                double shortRad = shortRadArr.get(i).isJsonNull() ? 0 : shortRadArr.get(i).getAsDouble();
                double directRad = directRadArr.get(i).isJsonNull() ? 0 : directRadArr.get(i).getAsDouble();
                double diffuseRad = diffuseRadArr.get(i).isJsonNull() ? 0 : diffuseRadArr.get(i).getAsDouble();
                int sunshine = sunshineArr.get(i).isJsonNull() ? 0 : sunshineArr.get(i).getAsInt();

                int isRain = precip > 0.1 ? 1 : 0;
                int hour = dateTime.getHour();
                int dayOfWeek = dateTime.getDayOfWeek().getValue();
                int isHoliday = HolidayUtil.getHolidayFlag(dateTime.toLocalDate());

                pstmt.setString(1, cityNo);
                pstmt.setString(2, cityName);
                pstmt.setString(3, dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")));
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
                pstmt.setDouble(15, shortRad);
                pstmt.setDouble(16, directRad);
                pstmt.setDouble(17, diffuseRad);
                pstmt.setInt(18, sunshine);

                pstmt.addBatch();
                if (i % 500 == 499) {
                    pstmt.executeBatch();
                    conn.commit();
                }
            }

            pstmt.executeBatch();
            conn.commit();
            System.out.println("完成：" + cityName + " 共" + total + "条");
        }
    }
}