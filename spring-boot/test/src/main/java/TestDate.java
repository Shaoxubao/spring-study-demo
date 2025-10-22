import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class TestDate {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        long diff = (long)(0.2 * 10);
        long localDateTime = now.minusHours(diff).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(localDateTime);

        long daymidnightMillis = LocalDate.now().minusDays((long)0).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("===daymidnightMillis:" + daymidnightMillis);

        // 使用ChronoUnit计算完整年数差异
        LocalDate date1 = LocalDate.of(2022, 9, 1);
        LocalDate date2 = LocalDate.of(2025, 8, 1);
        long yearsBetween = ChronoUnit.YEARS.between(date1, date2);
        System.out.println("Years between the two dates: " + yearsBetween);

        // 使用Period获取更详细的差异（年、月、日）
        Period period = Period.between(date1, date2);
        System.out.println("Years: " + period.getYears() + ", Months: " + period.getMonths() + ", Days: " + period.getDays());

        System.out.println("2025-09-09 14:00:00".substring(0, 7).replaceAll("-", ""));

        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        System.out.println(map);
    }
}
