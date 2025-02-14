package com.nari;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;   
import org.apache.flink.streaming.api.datastream.DataStream;   
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;   
import org.apache.flink.util.Collector;
   
public class WordCountLocal {
    public static void main(String[] args) throws Exception {
        // 1. 创建本地执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 定义输入数据（直接使用内存中的集合）
        DataStream<String> text = env.fromElements(
            "Apache Flink is a powerful stream processing framework",
            "Flink supports both batch and stream processing",
            "This is a simple WordCount example running locally"
        );
        // 3. 数据转换：分词、计数
        DataStream<Tuple2<String, Integer>> counts = text
            .flatMap(new Tokenizer())  // 将每行文本拆分为单词
            .keyBy(value -> value.f0)  // 按单词分组
            .sum(1);    // 对每个单词计数求和
        // 4. 输出结果到控制台
        counts.print();
        // 5. 触发任务执行
        env.execute("Local WordCount Example");
    }
    // 自定义分词器（将每行文本拆分为单词，生成 (word, 1) 键值对）
    public static class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            String[] words = value.toLowerCase().split("\\W+");  // 按非字母字符分割
            for (String word : words) {
                if (!word.isEmpty()) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        }
    }
}