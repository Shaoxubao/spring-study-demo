import com.alibaba.fastjson.JSONObject;
import utils.Consumer;
import utils.Repeater;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolTest {

    class Task implements Callable<Integer> {

        private int i;

        public Task(int i) {
            this.i = i;
        }
        @Override
        public Integer call() throws Exception {
            Thread.sleep(1000);
            return i;
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(100);

        List<Task> taskList = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            taskList.add(new ThreadPoolTest().new Task(i));
        }

        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tempList.add(i);
        }

        Repeater.consumeEach(2, tempList, new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> subList) {

            }
        });
        for (int i = 0; i < 3; i++) {
            Future future = executor.submit(taskList.get(i));
            System.out.println(future.get(50, TimeUnit.SECONDS));
        }

        executor.shutdown();
    }

}
