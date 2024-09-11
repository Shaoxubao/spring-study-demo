package com.baoge.utils;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 */
public class TimeUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
    public static String getPreDay(String date, int day) {
        try {
             return getPreDayMonthYear(date, day, 0, 0);
        } catch (ParseException e) {
            LOGGER.error("getPreDay error:", e);
        }
        return null;
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
    public static String getPreDayMonthYear(String date, int day, int month, int year) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(date));
        c.add(Calendar.DATE, day);
        c.add(Calendar.MONTH, month);
        c.add(Calendar.YEAR, year);
        Date m3 = c.getTime();
        return format.format(m3);
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

    public static String getPreMinute(String date, int min, String format) {
        SimpleDateFormat sdft = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = sdft.parse(date);
        } catch (ParseException e) {
            LOGGER.error("converToDate异常 date=" + date, e);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(result);
        c.add(Calendar.MINUTE, min);
        Date m3 = c.getTime();
        return sdft.format(m3);
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

    public static String converToString(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        String result = null;
        result = sf.format(date);
        return result;
    }
}
