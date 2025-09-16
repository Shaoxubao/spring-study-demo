package com.baoge.excel.bean;

import java.util.List;

public class VirtualPowerPlant {
    private String name;
    private List<TestRecord> testRecords;

    // Getter & Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestRecord> getTestRecords() {
        return testRecords;
    }

    public void setTestRecords(List<TestRecord> testRecords) {
        this.testRecords = testRecords;
    }
}