import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestDate {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        long diff = (long)(0.2 * 10);
        long localDateTime = now.minusHours(diff).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(localDateTime);

    }
}
