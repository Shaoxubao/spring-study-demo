package utils;

import java.util.List;

public class Repeater {
    public static <T> void consumeEach1000(List<T> list, Consumer<List<T>> consumer) throws Exception {
        consumeEach(1000, list, consumer);
    }

    public static <T> void consumeEach(int step, List<T> list, Consumer<List<T>> consumer) throws Exception {
        int index = 0;
        while (index < list.size()) {
            consumer.accept(list.subList(index, Math.min(index + step, list.size())));
            index += step;
        }
    }
}
