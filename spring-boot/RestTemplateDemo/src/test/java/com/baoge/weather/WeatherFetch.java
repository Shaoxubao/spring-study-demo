package com.baoge.weather;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.LocalDate;

public class WeatherFetch {

    // 西安经纬度
    private static final double LAT = 34.2646;
    private static final double LON = 108.9497;

    public static void main(String[] args) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3);

        LocalDate lastDay = endDate.minusDays(11);

        String url = "https://archive-api.open-meteo.com/v1/archive" +
                "?latitude=" + LAT +
                "&longitude=" + LON +
                "&start_date=" + endDate +
                "&end_date=" + endDate +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,precipitation,windspeed_10m" +
                "&timezone=Asia/Shanghai";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("请求失败：" + response.code());
                return;
            }

            String json = response.body().string();
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            JsonObject hourly = root.getAsJsonObject("hourly");

            // Java 8 标准写法，无 var
            JsonArray timeList = hourly.getAsJsonArray("time");
            JsonArray tempList = hourly.getAsJsonArray("temperature_2m");
            JsonArray humidityList = hourly.getAsJsonArray("relativehumidity_2m");
            JsonArray weatherCodeList = hourly.getAsJsonArray("weathercode");
            JsonArray precipList = hourly.getAsJsonArray("precipitation");
            JsonArray windList = hourly.getAsJsonArray("windspeed_10m");

            System.out.println("共获取数据：" + timeList.size() + "条");
            System.out.println("----------------------------------------");

            // 打印前10条
            for (int i = 0; i < timeList.size(); i++) {
                String time = timeList.get(i).getAsString().replace("T", " ");
                double temp = tempList.get(i).getAsDouble();
                int humidity = humidityList.get(i).getAsInt();
                int weatherCode = weatherCodeList.get(i).getAsInt();
                double precip = precipList.get(i).getAsDouble();
                double wind = windList.get(i).getAsDouble();
                int isRain = precip > 0 ? 1 : 0;

                System.out.printf("%s | 温度:%.1f | 湿度:%d | 天气码:%d | 下雨:%d | 风速:%.1f%n",
                        time, temp, humidity, weatherCode, isRain, wind);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}