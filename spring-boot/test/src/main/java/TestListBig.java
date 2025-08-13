import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestListBig {
    public static void main(String[] args) {
        List<BigDecimal> list = new ArrayList<>();
        list.add(new BigDecimal(2));
        list.add(new BigDecimal(3));
        list.add(new BigDecimal(4));

        // 假设将索引为 1 的元素置为 null
        int indexToSetNull = 1;
        list.set(indexToSetNull, null);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
