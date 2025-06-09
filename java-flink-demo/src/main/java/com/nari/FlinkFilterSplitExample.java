package com.nari;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;  
import org.apache.flink.streaming.api.datastream.DataStreamSource;  
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;  
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class FlinkFilterSplitExample {  
    public static void main(String[] args) throws Exception {  
        // 创建Flink执行环境  
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();  
  
        // 从Socket接收数据流（这里假设Socket发送的是整数数据）  
        DataStream<Integer> numberStream = env.addSource(new RandomNumberSource());
  
        // 将字符串数据流转换为整数数据流  
        SingleOutputStreamOperator<Integer> intStream = numberStream.map(Integer::valueOf);
  
        // 使用filter算子进行分流：偶数流和奇数流  
        SingleOutputStreamOperator<Integer> evenStream = intStream.filter(new FilterFunction<Integer>() {  
            @Override  
            public boolean filter(Integer value) throws Exception {  
                return value % 2 == 0;  
            }  
        });  
  
        SingleOutputStreamOperator<Integer> oddStream = intStream.filter(new FilterFunction<Integer>() {  
            @Override  
            public boolean filter(Integer value) throws Exception {  
                return value % 2 != 0;  
            }  
        });  
  
        // 打印偶数流和奇数流  
        evenStream.print("Even Stream: ");  
        oddStream.print("Odd Stream: ");  
  
        // 执行Flink程序  
        env.execute("Flink Filter Split Example");  
    }

    // 自定义 Source（生成1~100的随机数，每秒1条）
    public static class RandomNumberSource implements SourceFunction<Integer> {

        @Override
        public void run(SourceContext<Integer> ctx) throws Exception {
           for (int i = 0; i < 10; i++) {
                int num = (int) (Math.random() * 100) + 1; // 生成1个1~100的随机整数
                ctx.collect(num); // 发送数据
            }
        }

        @Override
        public void cancel() {

        }
    }
}
