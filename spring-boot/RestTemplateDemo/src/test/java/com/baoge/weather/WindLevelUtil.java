package com.baoge.weather;

public class WindLevelUtil {

    // 中国气象局标准：根据风速计算风力等级 0-12级
    public static int getWindLevel(double windSpeed) {
        if (windSpeed < 0.3) return 0;
        if (windSpeed < 1.6) return 1;
        if (windSpeed < 3.4) return 2;
        if (windSpeed < 5.5) return 3;
        if (windSpeed < 8.0) return 4;
        if (windSpeed < 10.8) return 5;
        if (windSpeed < 13.9) return 6;
        if (windSpeed < 17.2) return 7;
        if (windSpeed < 20.8) return 8;
        if (windSpeed < 24.5) return 9;
        if (windSpeed < 28.5) return 10;
        if (windSpeed < 32.6) return 11;
        return 12;
    }
}