import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestList {
    public static void main(String[] args) {
        List<String> consNoAllList= new ArrayList<>(Arrays.asList("2342432", "812323", "909898"));
        List<String> consNoList = new ArrayList<>(Arrays.asList("2342432", "908898"));
        consNoList.removeAll(consNoAllList);
        System.out.println(consNoList);
    }
}
