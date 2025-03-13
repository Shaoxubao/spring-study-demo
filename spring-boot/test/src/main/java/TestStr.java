import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestStr {
    public static void main(String[] args) {
        String str = ",p1,p2,p3";
        String[] split = str.substring(1).split(",");
        System.out.println(Arrays.toString(split));

        int count = 10;
        int i = 0;
        while (i < 10) {
            System.out.println("============");
            if (i == 5) {
                System.out.println("out===========");
                break;
            }
            i++;
        }
        System.out.println("end");

        List<JSONObject> originalList = new ArrayList<>();
        JSONObject curve1 = new JSONObject();
        curve1.put("a", "1");
        JSONObject curve2 = new JSONObject();
        curve2.put("b", "1");
        originalList.add(curve1);
        originalList.add(curve2);

        List<JSONObject> tempList = new ArrayList<>(originalList);
        System.out.println(originalList);
        System.out.println(tempList);
        System.out.println(originalList == tempList);

        String s = "123 No operations to perform.";
        System.out.println(s.contains("No operations"));


        String abc = "abc";
        System.out.println(abc.substring(0, 1));   // a
        System.out.println(abc.substring(1));  // bc

        String whole = "10101000";
        System.out.println(Arrays.toString(whole.split("")));
    }
}
