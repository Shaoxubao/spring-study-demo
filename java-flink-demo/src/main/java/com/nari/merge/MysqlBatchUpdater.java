package com.nari.merge;

import java.util.*;
import java.util.stream.Collectors;

public class MysqlBatchUpdater {

    static class DataGroup {
        private final List<Double> data;
        private final int points;
        private final String whole;
        private final int start;

        public DataGroup(List<Double> data, int points, String whole, int start) {
            this.data = data;
            this.points = points;
            this.whole = whole;
            this.start = start;
        }

        public List<Double> getData() {
            return data;
        }

        public int getPoints() {
            return points;
        }

        public String getWhole() {
            return whole;
        }

        public int getStart() {
            return start;
        }
    }

    static class SqlResult {
        private final String sql;
        private final List<Object> params;

        public SqlResult(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }

        @Override
        public String toString() {
            return "SqlResult{" +
                    "sql='" + sql + '\'' +
                    ", params=" + params +
                    '}';
        }
    }

    public static SqlResult buildBatchSql(List<DataGroup> dataGroups, int uniqueId) {
        // 收集所有涉及的p字段值和flag位置
        Map<String, Double> pFields = new LinkedHashMap<>();
        Set<Integer> flagPositions = new TreeSet<>();

        for (DataGroup group : dataGroups) {
            validateGroup(group);
            int start = group.getStart();
            int points = group.getPoints();
            List<Double> data = group.getData();

            for (int i = 0; i < points; i++) {
                int pNum = start + i;
                String field = "p" + pNum;
                pFields.put(field, data.get(i));
                flagPositions.add(pNum - 1); // p1对应flag索引0
            }
        }

        // 合并连续的flag位置区间
        List<Interval> intervals = mergeIntervals(new ArrayList<>(flagPositions));

        // 生成p字段的SET子句和参数
        List<String> setClauses = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        for (Map.Entry<String, Double> entry : pFields.entrySet()) {
            setClauses.add(entry.getKey() + " = ?");
            params.add(entry.getValue());
        }

        // 生成flag字段的SET子句
        String flagSetClause = generateFlagSetClause(intervals);
        setClauses.add(flagSetClause);

        // 构建完整的UPDATE语句（假设唯一键为id）
        String sql = "UPDATE point_data SET " + String.join(", ", setClauses) + " WHERE id = ?";
        params.add(uniqueId); // 添加唯一键条件

        return new SqlResult(sql, params);
    }

    private static void validateGroup(DataGroup group) {
        int start = group.getStart();
        int points = group.getPoints();
        if (start < 1 || start > 96 || points < 1 || start + points - 1 > 96) {
            throw new IllegalArgumentException("Invalid start/points: start=" + start + ", points=" + points);
        }
        String whole = group.getWhole();
        if (whole.length() != points || !whole.chars().allMatch(c -> c == '1')) {
            throw new IllegalArgumentException("Invalid whole: " + whole);
        }
        List<Double> data = group.getData();
        if (data.size() != points) {
            throw new IllegalArgumentException("Data size mismatch: " + data.size() + " vs " + points);
        }
    }

    private static List<Interval> mergeIntervals(List<Integer> positions) {
        if (positions.isEmpty()) return Collections.emptyList();
        positions.sort(Integer::compare);
        List<Interval> intervals = new ArrayList<>();
        int currentStart = positions.get(0);
        int currentEnd = positions.get(0);
        for (int i = 1; i < positions.size(); i++) {
            int pos = positions.get(i);
            if (pos == currentEnd + 1) {
                currentEnd = pos;
            } else {
                intervals.add(new Interval(currentStart, currentEnd));
                currentStart = pos;
                currentEnd = pos;
            }
        }
        intervals.add(new Interval(currentStart, currentEnd));
        return intervals;
    }

    static class Interval {
        final int start;
        final int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private static String generateFlagSetClause(List<Interval> intervals) {
        StringBuilder sb = new StringBuilder("CONCAT(");
        int prevEnd = -1;
        boolean first = true;

        for (Interval interval : intervals) {
            int currStart = interval.start;
            int currEnd = interval.end;

            // 前半部分未覆盖的flag位
            if (currStart > prevEnd + 1) {
                if (!first) sb.append(", ");
                sb.append("SUBSTRING(flag, ").append(prevEnd + 1).append(", ").append(currStart - prevEnd - 1).append(")");
                first = false;
            }

            // 当前覆盖的位设为1
            if (!first) sb.append(", ");
            sb.append("'").append(StringRepeat.repeat("1", (currEnd - currStart + 1))).append("'");
            first = false;

            prevEnd = currEnd;
        }

        // 最后未覆盖的flag位
        if (prevEnd < 95) {
            if (!first) sb.append(", ");
            sb.append("SUBSTRING(flag, ").append(prevEnd + 1).append(", ").append(96 - prevEnd - 1).append(")");
        }

        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        // 示例数据组
        List<DataGroup> dataGroups = Arrays.asList(
                new DataGroup(Arrays.asList(0.008, 0.1, 0.2, 0.3), 4, "1111", 8),
                new DataGroup(Arrays.asList(0.5, 0.6, 0.7, 0.8), 4, "1111", 15),
                new DataGroup(Arrays.asList(0.15, 0.16, 0.17, 0.18), 4, "1111", 21)
        );

        // 构建SQL（假设唯一键id=1）
        SqlResult result = buildBatchSql(dataGroups, 1);
        System.out.println("Generated SQL: " + result);
    }
}