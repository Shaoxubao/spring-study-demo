package com.baoge.excel.bean;

import java.util.List;

public class TestRecord {
    private String applyDate;   // 测试申请日期
    private String passDate;   // 测试通过日期
    private double upCapacity; // 调峰调上能力(MW)
    private double downCapacity; // 调峰调下能力(MW)
    private double spotCapacity; // 现货校核能力(MW)
    private List<HouseInfo> houses; // 关联的户信息

    // Getter & Setter

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getPassDate() {
        return passDate;
    }

    public void setPassDate(String passDate) {
        this.passDate = passDate;
    }

    public double getUpCapacity() {
        return upCapacity;
    }

    public void setUpCapacity(double upCapacity) {
        this.upCapacity = upCapacity;
    }

    public double getDownCapacity() {
        return downCapacity;
    }

    public void setDownCapacity(double downCapacity) {
        this.downCapacity = downCapacity;
    }

    public double getSpotCapacity() {
        return spotCapacity;
    }

    public void setSpotCapacity(double spotCapacity) {
        this.spotCapacity = spotCapacity;
    }

    public List<HouseInfo> getHouses() {
        return houses;
    }

    public void setHouses(List<HouseInfo> houses) {
        this.houses = houses;
    }
}