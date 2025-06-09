package com.nari;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.ArrayList;
import java.util.List;

public class FlinkFilterSplitExample2 {
    public static void main(String[] args) throws Exception {  
        // 创建Flink执行环境  
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 显式设置处理时间特征
//        env.setStreamTimeCharacteristic(org.apache.flink.streaming.api.TimeCharacteristic.ProcessingTime);
        // 生成订单数据流
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("1", "user1", "merchant1", 100.0));
        orders.add(new Order("2", "user2", "merchant2", 200.0));
        orders.add(new Order("3", "user1", "merchant1", 300.0));
        orders.add(new Order("4", "user2", "merchant2", 400.0));
        DataStream<Order> orderStream = env.fromCollection(orders);


        DataStream<Tuple3<String, String, Double>> resultStream = orderStream
                .keyBy(order -> order.userId)
                .sum("amount")
                .map(order -> Tuple3.of(order.userId, order.merchantId, order.amount))
                .returns(TypeInformation.of(new TypeHint<Tuple3<String, String, Double>>() {}));

        // 打印结果
        resultStream.print();

  
        // 执行Flink程序  
        env.execute("Flink Filter Split Example");  
    }

    // 定义订单数据类
    public static class Order {
        public String orderId;
        public String userId;
        public String merchantId;
        public double amount;

        public Order(String orderId, String userId, String merchantId, double amount) {
            this.orderId = orderId;
            this.userId = userId;
            this.merchantId = merchantId;
            this.amount = amount;
        }
    }
}
