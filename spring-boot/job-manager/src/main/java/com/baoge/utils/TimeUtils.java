package com.baoge.utils;

import cn.hutool.core.date.DateUtil;
import com.baoge.common.BusinessException;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 龚士宇
 * @version 1.0
 * @create 2021/10/12 11:45
 */
public class TimeUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);

    public static String getCurrentDateTime() {
        return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentDateTime(String dataFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
        return dateFormat.format(new Date());
    }

    public static String getCurrentYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(new Date());
    }


    public static Long getTimeStamp() {

        return System.currentTimeMillis() / 1000;
    }


    /**
     * 日期加减
     *
     * @param date 日期
     * @param day  +加天  -减天
     * @return
     */
    public static String getPreDay(Date date, int day) {
        try {
            return getPreDayMonthYear(date, day, 0, 0);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getPreHourTime(String date, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.HOUR, hour);
        Date date2 = c.getTime();
        return format.format(date2);
    }

    public static String transFormatChinese(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format1 = new SimpleDateFormat("yyyy年MM月dd日");
        return format1.format(parse);
    }

    /**
     * 转换为日期格式
     *
     * @param date
     * @return
     */
    public static Date converToDate(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = sf.parse(date);
        } catch (ParseException e) {
            LOGGER.error("converToDate异常 date=" + date, e);
        }
        return result;
    }

    public static Long convertToTimeStamp(String dateStr) {
        Date date = converToDate(dateStr);
        return date.getTime() / 1000;
    }

    public static Long convertToTimeStamp(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 当前日期做加减  -的表示向前 正的表示向后 0 表示不变
     *
     * @throws ParseException
     */
    public static String getPreDayMonthYear(Date date, int day, int month, int year) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        c.add(Calendar.MONTH, month);
        c.add(Calendar.YEAR, year);
        Date m3 = c.getTime();
        return format.format(m3);
    }


    /**
     * 当前日期做加减  -的表示向前 正的表示向后 0 表示不变
     *
     * @throws ParseException
     */
    public static String getPreMinute(Date date, int min) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, min);
        Date m3 = c.getTime();
        return format.format(m3);
    }

    /**
     * 转换日期格式
     *
     * @return
     */
    public static String convertDateTime(String date, String format) {
        Date _date = converToDate(date);
        SimpleDateFormat _format = new SimpleDateFormat(format);
        return _format.format(_date);
    }

    /**
     * 转换日期格式
     *
     * @return
     */
    public static String convertDateToYear(String date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try {
            result = sf.parse(date);
        } catch (ParseException e) {
            LOGGER.error("converToDate 异常 date=" + date, e);
        }
        SimpleDateFormat _format = new SimpleDateFormat(format);
        return _format.format(result);
    }

    /**
     * 获取0点到现在一共多少分钟
     *
     * @return
     */
    public static int getCurMinute() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String curTime = format.format(now);
        return getCurMinuteByTime(curTime);
    }

    /**
     * 获取0点到现在一共多少分钟
     *
     * @param dateTime "HH:mm:ss"
     * @return
     */
    public static int getCurMinuteByTime(String dateTime) {
        if (dateTime.length() > "HH:mm:ss".length()) {
            throw new BusinessException("时间格式不是 HH:mm:ss");
        }
        int curHour = Integer.parseInt(dateTime.substring(0, 2));
        int curMin = Integer.parseInt(dateTime.substring(3, 5));
        int curTimePoint = 60 * curHour + curMin;
        return curTimePoint;
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
     * 获取当前日期属于第几个15分钟
     *
     * @return
     */
    public static int getPointNumBy15(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return TimeUtils.getPointNumByMinute(dateTime, 15);
        }
        return 0;
    }


    /**
     * @param dateStr
     * @return int
     * @Description 获取指定日期是当年的第几天
     * @author sunyu
     * @date 2022/8/23 15:22
     */
    public static int dayOfYear(String dateStr) {
        return DateUtil.dayOfYear(converToDate(dateStr));
    }


    /**
     * 根据点位转日期
     *
     * @param point       如:P1 则传1
     * @param groupMinute 分钟分组 比如: 5分钟一个点 则groupMinute = 5
     * @return
     */
    public static String convertTimeByPoint(int point, int groupMinute) {
        int _point = ((point - 1) * groupMinute);
        int hour = _point / 60;
        int minute = _point % 60;
        return addLeftZero(hour, 2) + ":" + addLeftZero(minute, 2);
    }

    //对map进行排序放入list
    public static List convertList(Map<String, BigDecimal> pointMap, int maxPoint) {

        List list = new ArrayList<BigDecimal>();
        for (int i = 1; i <= maxPoint; i++) {
            list.add(MapUtils.getString(pointMap, 'p' + i, "null"));
        }
        return list;
    }


    //获取横坐标时间点(写死的)
    public static List getTimeListBy15() {
        List list = new ArrayList<Float>();
        for (int i = 1; i <= 96; i++) {
            list.add(convertTimeByPoint(i, 15));
        }
        return list;
    }

    /**
     * 补全序列左边的0，比如1->001
     *
     * @param sequence    绝对值
     * @param totalLength 总长度
     * @return
     */
    private static String addLeftZero(long sequence, int totalLength) {
        int needZeroLength = totalLength - String.valueOf(sequence).length();
        return String.valueOf((long) Math.pow(10, needZeroLength))
                .substring(1) + sequence;
    }


    /**
     * @param startDateStr YYYY-MM-dd
     * @param endDateStr
     * @return java.util.List<java.lang.String>
     * @Description 获取指定日期间的每一天
     * @author SunYu
     * @date 2021/12/29 11:06
     **/
    public static List<String> convertToEveryDay(String startDateStr, String endDateStr) {
        if (StringUtils.isBlank(startDateStr)) {
            return new ArrayList<>();
        } else if (startDateStr.equals(endDateStr) || StringUtils.isBlank(endDateStr)) {
            return Lists.newArrayList(startDateStr);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dBegin = sdf.parse(startDateStr);
            Date dEnd = sdf.parse(endDateStr);
            return findDates(dBegin, dEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList(startDateStr);
    }

    private static List<String> findDates(Date dBegin, Date dEnd) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List lDate = new ArrayList();
        lDate.add(dateFormat.format(dBegin));

        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);

        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);

        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(dateFormat.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 转换为日期格式字符串
     *
     * @param date
     * @return
     */
    public static String converToString(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = null;
        result = sf.format(date);
        return result;
    }

    public static Long transForMinute(long seconds) {
        return seconds / 60;
    }

    /**
     * @param startTime
     * @param timeType
     * @return Integer
     * @Description 输入时间和当前时间相差多少天或分钟
     * @author sunyu
     * @date 2022/10/11 9:45
     */
    public static Integer compareToNow(String startTime, String timeType) {
        Date date = converToDate(startTime);
        Long betweenSecond = date.getTime() - (System.currentTimeMillis());
        switch (timeType) {
            case "day"://比较startTime和今天相差几天
                return Integer.valueOf(String.valueOf(betweenSecond / (1000 * 60 * 60 * 24)));
            case "hour"://比较startTime和现在相差多少小时
                return Integer.valueOf(String.valueOf(betweenSecond / (1000 * 60 * 60)));
            case "min"://比较startTime和现在相差多少分钟
                return Integer.valueOf(String.valueOf(betweenSecond / (1000 * 60)));
            default:
                ;
        }
        return 0;
    }
}
