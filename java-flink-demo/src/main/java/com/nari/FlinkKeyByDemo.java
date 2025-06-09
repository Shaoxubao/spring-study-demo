package com.nari;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlinkKeyByDemo {
    public static void main(String[] args) throws Exception {
        //TODO 创建Flink上下文执行环境
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度为1
        streamExecutionEnvironment.setParallelism(1);
        //设置执行模式为批处理
        streamExecutionEnvironment.setRuntimeMode(RuntimeExecutionMode.BATCH);
        //TODO source 从集合中创建数据源
        DataStreamSource<String> dataStreamSource = streamExecutionEnvironment.fromElements("hello word", "hello flink");
        //TODO 方式一 匿名实现类
        SingleOutputStreamOperator<Tuple2<String, Integer>> outputStreamOperator1 = dataStreamSource
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String s, Collector<String> collector) throws Exception {
                        String[] s1 = s.split(" ");
                        for (String word : s1) {
                            collector.collect(word);
                        }
                    }
                })
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String s) throws Exception {
                        Tuple2<String, Integer> aa = Tuple2.of(s, 1);
                        return aa;
                    }
                })
                .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                        return stringIntegerTuple2.f0;
                    }
                })
                .sum(1);
        //TODO 方式二 Lambda表达式实现
        SingleOutputStreamOperator<Tuple2<String, Integer>> outputStreamOperator2 = dataStreamSource
                .flatMap((String s, Collector<String> collector) -> {
                    String[] s1 = s.split(" ");
                    for (String word : s1) {
                        collector.collect(word);
                    }
                })
                .returns(Types.STRING)
                .map((String word) -> {
                    return Tuple2.of(word, 1);
                })
                //Java中lambda表达式存在类型擦除
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy((Tuple2<String, Integer> s) -> {
                    return s.f0;
                })
                .sum(1);
        //TODO sink
        outputStreamOperator1.print("方式一");
        outputStreamOperator2.print("方式二");

        // 执行Flink程序
        streamExecutionEnvironment.execute("Flink Filter Split Example");
    }
}
