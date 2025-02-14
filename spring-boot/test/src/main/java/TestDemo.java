import com.alibaba.fastjson.JSONObject;

public class TestDemo {
    public static void main(String[] args) {
        int i;
        for (i = 1; i <= 10; i++) {
            if (executeBatch()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        if (i >= 10) {
            System.out.println("executeBatch retry failed: " + JSONObject.toJSON("sublist").toString());
        }
    }

    public static boolean executeBatch() {
        return false;
    }
}
