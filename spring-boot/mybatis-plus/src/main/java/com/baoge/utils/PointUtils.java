package com.baoge.utils;

import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author wangxing
 * @version 1.0
 * @create 2023/10/07 11:45
 */
@Slf4j
public class PointUtils {


    /**
     * 获取当前日期属于第几个15分钟 向上取整
     *
     * @param dateTime    "HH:mm"
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
            return PointUtils.getPointNumByMinute(dateTime, 15);
        }
        return 0;
    }



    public static int getCurMinuteByTime(String dateTime) {
        if (dateTime.length() > "HH:mm".length()) {
            return 0;
        }
        int curHour = Integer.parseInt(dateTime.substring(0, 2));
        int curMin = Integer.parseInt(dateTime.substring(3, 5));
        int curTimePoint = 60 * curHour + curMin;
        return curTimePoint;
    }


    public static void main(String[] args) {
        System.out.println(getPointNumBy15("12:15"));
    }

}
