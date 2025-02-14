package queue;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeTest {
    public static void main(String[] args) {
        ConcurrentLinkedDeque<String> queue = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < 10; i++) {
            queue.add("" + i);
        }
        while (!Thread.interrupted()) {
            try {
                String result = queue.poll();
                System.out.println(result);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
