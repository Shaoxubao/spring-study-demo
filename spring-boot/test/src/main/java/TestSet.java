import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestSet {
    public static void main(String[] args) {
        Set<String> loginUserConcurrentSet = ConcurrentHashMap.newKeySet();
        loginUserConcurrentSet.add("a");
        loginUserConcurrentSet.add("b");
        loginUserConcurrentSet.add("c");

        System.out.println(loginUserConcurrentSet.remove("a"));
        System.out.println(loginUserConcurrentSet.toString());
        System.out.println(loginUserConcurrentSet.remove("d"));
    }
}
