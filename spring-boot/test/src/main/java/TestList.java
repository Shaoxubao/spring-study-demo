import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestList {
    public static void main(String[] args) {
        List<String> consNoAllList= new ArrayList<>(Arrays.asList("2342432", "812323", "909898"));
        List<String> consNoList = new ArrayList<>(Arrays.asList("2342432", "908898"));
//        consNoList.removeAll(consNoAllList);
        System.out.println(consNoList);

        consNoList.add(1, null);

        System.out.println(consNoList);

        BigDecimal a = new BigDecimal(6600);
        BigDecimal b = new BigDecimal(500);
        BigDecimal c = a.add(b).divide(new BigDecimal(1000), 2,  RoundingMode.HALF_UP);
        System.out.println(c);

        BigDecimal d = new BigDecimal(0);
        System.out.println(d.compareTo(new BigDecimal(0)));

    }
}
