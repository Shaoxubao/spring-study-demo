package com.nari;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;   
import org.apache.flink.api.common.functions.ReduceFunction;   
import org.apache.flink.streaming.api.datastream.DataStream;   
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;   
import org.apache.flink.streaming.api.functions.sink.SinkFunction;   
import org.apache.flink.streaming.api.functions.source.SourceFunction;
   
public class LocalFlinkPipeline {

    public static void main(String[] args) throws Exception {
        // 1. 创建本地执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1); // 设置并行度为1，方便观察输出

        // 2. 自定义 Source：生成随机数字
        DataStream<Integer> numberStream = env.addSource(new RandomNumberSource())
            .name("random-number-source"); // 指定算子名称

        // 3. 算子操作链
        DataStream<String> processedStream = numberStream
            // (1) Map：将数字转换为字符串
            .map(new MapFunction<Integer, String>() {
                @Override
                public String map(Integer value) {
                    return "Number-" + value;
                }
            })
            .name("number-to-string-map")
            
            // (2) Filter：过滤掉偶数
            .filter(new FilterFunction<String>() {
                @Override
                public boolean filter(String value) {
                    int num = Integer.parseInt(value.split("-")[1]);
                    return num % 2 != 0; // 过滤掉偶数,保留奇数
                }
            })
            .name("odd-number-filter")
            
            // (3) KeyBy：按字符串长度分组
            .keyBy(value -> value.length())
            
            // (4) Reduce：统计每组的最大值
            .reduce(new ReduceFunction<String>() {
                @Override
                public String reduce(String value1, String value2) {
                    int num1 = Integer.parseInt(value1.split("-")[1]);
                    int num2 = Integer.parseInt(value2.split("-")[1]);
                    return num1 > num2 ? value1 : value2;
                }
            })
            .name("group-max-reducer");

        // 4. 自定义 Sink：打印处理结果
        processedStream.addSink(new CustomPrintSink())
            .name("custom-print-sink");

        // 5. 执行任务
        env.execute("Local Flink Pipeline Demo");
    }

    // ----------------------------------------------------
    // 自定义 Source（生成1~100的随机数，每秒1条）
    public static class RandomNumberSource implements SourceFunction<Integer> {
        private volatile boolean isRunning = true;

        @Override
        public void run(SourceContext<Integer> ctx) throws Exception {
            while (isRunning) {
                int num = (int) (Math.random() * 100) + 1; // 生成1个1~100的随机整数
                ctx.collect(num); // 发送数据
                Thread.sleep(1000); // 间隔1秒
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    // ----------------------------------------------------
    // 自定义 Sink（带前缀输出）
    public static class CustomPrintSink implements SinkFunction<String> {
        @Override
        public void invoke(String value, Context context) {
            System.out.println("[Custom Sink] >>> " + value);
        }
    }   
}
