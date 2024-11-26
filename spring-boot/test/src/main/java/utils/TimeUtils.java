package utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

public class TimeUtils {

    public static String getHourMinute(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour + "-" + minute;
    }

    public static String getHourMinuteSecond(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String hourStr = hour + "";
        String minuteStr = minute + "";
        String secondStr = second + "";

        if (getTimeLength(hour) == 1) {
            hourStr = "0" + hour;
        }
        if (getTimeLength(minute) == 1) {
            minuteStr = "0" + minute;
        }
        if (getTimeLength(second) == 1) {
            secondStr = "0" + second;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    private static int getTimeLength(int number) {
        return String.valueOf(number).length();
    }

    /**
     * 获取当前日期属于第几个15分钟
     *
     * @param dateTime "HH:mm:ss"
     * @return
     */
    public static int getPointNumBy15(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return TimeUtils.getPointNumByMinute(dateTime, 15);
        }
        return 0;
    }

    /**
     * 获取当前日期属于第几个15分钟
     *
     * @param dateTime    "HH:mm:ss"
     * @param groupMinute 多少分钟一组
     * @return
     */
    public static int getPointNumByMinute(String dateTime, int groupMinute) {
        // 获取传入的时间是今天的第几分钟
        int minute = getCurMinuteByTime(dateTime);
        // 先判断传入的时间是否是整点（判断条件：能被15整除则是整点，否则：则不是）
        int moduleResult = minute % groupMinute;
        int point = minute / groupMinute;
        // 取模判断传入的时间点是否整点
        if (moduleResult > 0) {
            // 若不是整点，则点位加1
            point = point + 1;
        }
        return point;
    }

    /**
     * 获取0点到现在一共多少分钟
     *
     * @param dateTime "HH:mm:ss"
     * @return
     */
    public static int getCurMinuteByTime(String dateTime) {
        if (dateTime.length() > "HH:mm:ss".length()) {
            throw new RuntimeException("时间格式不是 HH:mm:ss");
        }
        int curHour = Integer.parseInt(dateTime.substring(0, 2));
        int curMin = Integer.parseInt(dateTime.substring(3, 5));
        int curTimePoint = 60 * curHour + curMin;
        return curTimePoint;
    }

    public static void main(String[] args) {
        String curTime = getHourMinuteSecond(1715159797000L);
        System.out.println(curTime);
        System.out.println(getPointNumBy15(curTime));
    }

}
