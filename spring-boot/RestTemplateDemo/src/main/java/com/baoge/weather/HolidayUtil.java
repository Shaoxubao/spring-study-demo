package com.baoge.weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class HolidayUtil {
    private static final Set<String> HOLIDAY = new HashSet<>();
    private static final Set<String> WORKDAY = new HashSet<>();

    static {
        // 2025
        HOLIDAY.add("2025-01-01");
        HOLIDAY.add("2025-01-28"); HOLIDAY.add("2025-01-29"); HOLIDAY.add("2025-01-30");
        HOLIDAY.add("2025-01-31"); HOLIDAY.add("2025-02-01"); HOLIDAY.add("2025-02-02"); HOLIDAY.add("2025-02-03");
        HOLIDAY.add("2025-04-05");
        HOLIDAY.add("2025-05-01"); HOLIDAY.add("2025-05-02"); HOLIDAY.add("2025-05-03"); HOLIDAY.add("2025-05-04"); HOLIDAY.add("2025-05-05");
        HOLIDAY.add("2025-06-02");
        HOLIDAY.add("2025-10-01"); HOLIDAY.add("2025-10-02"); HOLIDAY.add("2025-10-03");
        HOLIDAY.add("2025-10-04"); HOLIDAY.add("2025-10-05"); HOLIDAY.add("2025-10-06"); HOLIDAY.add("2025-10-07");

        WORKDAY.add("2025-01-26"); WORKDAY.add("2025-02-08");
        WORKDAY.add("2025-04-27"); WORKDAY.add("2025-05-10");
        WORKDAY.add("2025-09-28"); WORKDAY.add("2025-10-11");

        // 2026
        HOLIDAY.add("2026-01-01");
        HOLIDAY.add("2026-01-22"); HOLIDAY.add("2026-01-23"); HOLIDAY.add("2026-01-24");
        HOLIDAY.add("2026-01-25"); HOLIDAY.add("2026-01-26"); HOLIDAY.add("2026-01-27"); HOLIDAY.add("2026-01-28");
        HOLIDAY.add("2026-04-05");
        HOLIDAY.add("2026-05-01"); HOLIDAY.add("2026-05-02"); HOLIDAY.add("2026-05-03"); HOLIDAY.add("2026-05-04"); HOLIDAY.add("2026-05-05");
        HOLIDAY.add("2026-06-19");
        HOLIDAY.add("2026-09-26");
        HOLIDAY.add("2026-10-01"); HOLIDAY.add("2026-10-02"); HOLIDAY.add("2026-10-03");
        HOLIDAY.add("2026-10-04"); HOLIDAY.add("2026-10-05"); HOLIDAY.add("2026-10-06"); HOLIDAY.add("2026-10-07");
    }

    public static boolean isHoliday(LocalDate date) {
        String d = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (HOLIDAY.contains(d)) return true;
        if (WORKDAY.contains(d)) return false;
        int v = date.getDayOfWeek().getValue();
        return v == 6 || v == 7;
    }

    public static int getHolidayFlag(LocalDate date) {
        return isHoliday(date) ? 1 : 0;
    }
}