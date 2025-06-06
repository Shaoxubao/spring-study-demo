package com.nari.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataGroup {
    private Long id;                // 主键之一
    private String dataTime; // 主键之二
    private List<Double> data;      // 数据值列表
    private int points;             // 数据点数
    private String whole;           // 校验标记（可选）
    private int start;              // 起始p点（1-based，如8代表p8）

    public DataGroup(Long id, String dataTime, List<Double> data, int points, String whole, int start) {
        this.id = id;
        this.dataTime = dataTime;
        this.data = data;
        this.points = points;
        this.whole = whole;
        this.start = start;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getWhole() {
        return whole;
    }

    public void setWhole(String whole) {
        this.whole = whole;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public static List<DataGroup> getDataGroups() {
        // 示例数据
        List<DataGroup> dataGroups = new ArrayList<>();
        DataGroup item1 = new DataGroup(1L, "2025-06-05", Arrays.asList(0.1, 0.2, 0.3, 0.4), 4, "1111", 5);
        DataGroup item2 = new DataGroup(1L, "2025-06-05", Arrays.asList(0.6, 0.12, 0.13, 0.14), 4, "1111", 9);
        DataGroup item3 = new DataGroup(1L, "2025-06-05", Arrays.asList(0.6, 0.12, 0.13, 0.14), 4, "1111", 20);
        dataGroups.add(item1);
        dataGroups.add(item2);
//        dataGroups.add(item3);
        return dataGroups;
    }
}