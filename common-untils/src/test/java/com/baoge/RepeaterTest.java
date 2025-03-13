package com.baoge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class RepeaterTest {

    @Test
    public void testConsumeEach() throws Exception {
        // 创建测试数据
        List<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");

        // 定义消费者函数
        Consumer<List<String>> consumer = subList -> {
            for (String item : subList) {
                System.out.println(item);
            }
        };

        // 测试正常情况
        System.out.println("step=2");
        Repeater.consumeEach(2, list, consumer);

        // 测试边界情况：步长为 1
        System.out.println("step=1");
        Repeater.consumeEach(1, list, consumer);

        // 测试边界情况：步长大于列表长度
        System.out.println("step=10");
        Repeater.consumeEach(10, list, consumer);
    }
}