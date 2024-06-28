package com.baoge.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Date curDate = new Date();
        String startTime = DateUtil.offsetHour(curDate, 1).toString();
        String endTime = DateUtil.format(curDate, DatePattern.NORM_DATETIME_FORMAT);


        Thread.sleep(60 * 1000);
        long diff = DateUtil.between(new Date(), curDate, DateUnit.MINUTE);
        System.out.println(diff);
    }
}
