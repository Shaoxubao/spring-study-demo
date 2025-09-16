package com.baoge.excel.bean;

public class HouseInfo {
    private String houseNo;       // 户号
    private String houseName;     // 户名
    private String installedCapacity; // 报装容量(MW)
    private String industry;      // 所属行业
    private String industryAdjustable; // 行业可调节容量(MW)
    private String maxUp;         // 校核最大上调能力
    private String maxDown;       // 校核最大下调能力

    // Getter & Setter

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(String installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustryAdjustable() {
        return industryAdjustable;
    }

    public void setIndustryAdjustable(String industryAdjustable) {
        this.industryAdjustable = industryAdjustable;
    }

    public String getMaxUp() {
        return maxUp;
    }

    public void setMaxUp(String maxUp) {
        this.maxUp = maxUp;
    }

    public String getMaxDown() {
        return maxDown;
    }

    public void setMaxDown(String maxDown) {
        this.maxDown = maxDown;
    }
}