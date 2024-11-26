import com.alibaba.fastjson.JSONObject;
import utils.TimeUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatePointUtils {


    public static void main(String[] args) {
        Map<Integer, Map<String, String>> pointMap = new HashMap<>();
        int p = 1;
        for (int i = 0; i < 24; i++) {
            Map<String, String> pointDataMap = new LinkedHashMap<>();
            pointDataMap.put("0-15", "p" + p++);
            pointDataMap.put("16-30", "p" + p++);
            pointDataMap.put("31-45", "p" + p++);
            pointDataMap.put("46-60", "p" + p++);
            pointMap.put(i, pointDataMap);
        }
        System.out.println(pointMap);

        System.out.println(getPoint(System.currentTimeMillis(), pointMap));

        long midnightMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("midnightMillis: " + midnightMillis);

        // 当前时间戳
        long currentMillis = System.currentTimeMillis();
        System.out.println("currentMillis: " + currentMillis);

        JSONObject curve = new JSONObject();
        curve.put("dataWholeFlag", "000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000");
        System.out.println(curve.getString("dataWholeFlag"));
    }

    private static String getPoint(long ts, Map<Integer, Map<String, String>> pointMap) {
        String hourMinute = TimeUtils.getHourMinute(ts);
        Integer hour = Integer.parseInt(hourMinute.split("-")[0]);
        Integer minute = Integer.parseInt(hourMinute.split("-")[1]);
        String minutePart = null;
        if (minute >= 0 && minute <= 15) {
            minutePart = "0-15";
        } else if (minute > 15 && minute <= 30) {
            minutePart = "16-30";
        } else if (minute > 30 && minute <= 45) {
            minutePart = "31-45";
        } else if (minute > 45 && minute <= 60) {
            minutePart = "46-60";
        }
        return pointMap.get(hour).get(minutePart);
    }
}
