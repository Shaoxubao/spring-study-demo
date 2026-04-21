package com.baoge.weather;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class WeatherDataHour {
    private Long id;
    private LocalDateTime time;
    private String cityNo;
    private String cityName;
    private BigDecimal loadMw;
    private BigDecimal temp;
    private Integer humidity;
    private Integer weatherCode;
    private Integer isRain;
    private Integer hour;
    private Integer dayOfWeek;
    private Integer isHoliday;
    private BigDecimal shortwaveRadiation;
    private BigDecimal directRadiation;
    private BigDecimal diffuseRadiation;
    private Integer sunshineDuration;
    private Integer windLevel;
    private BigDecimal windDirection;
    private BigDecimal windGusts;
    private BigDecimal windSpeed;
    private Date createTime;

       // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public String getCityNo() { return cityNo; }
    public void setCityNo(String cityNo) { this.cityNo = cityNo; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public BigDecimal getLoadMw() { return loadMw; }
    public void setLoadMw(BigDecimal loadMw) { this.loadMw = loadMw; }
    public BigDecimal getTemp() { return temp; }
    public void setTemp(BigDecimal temp) { this.temp = temp; }
    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }
    public Integer getWeatherCode() { return weatherCode; }
    public void setWeatherCode(Integer weatherCode) { this.weatherCode = weatherCode; }
    public Integer getIsRain() { return isRain; }
    public void setIsRain(Integer isRain) { this.isRain = isRain; }
    public Integer getHour() { return hour; }
    public void setHour(Integer hour) { this.hour = hour; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getIsHoliday() { return isHoliday; }
    public void setIsHoliday(Integer isHoliday) { this.isHoliday = isHoliday; }
    public BigDecimal getShortwaveRadiation() { return shortwaveRadiation; }
    public void setShortwaveRadiation(BigDecimal shortwaveRadiation) { this.shortwaveRadiation = shortwaveRadiation; }
    public BigDecimal getDirectRadiation() { return directRadiation; }
    public void setDirectRadiation(BigDecimal directRadiation) { this.directRadiation = directRadiation; }
    public BigDecimal getDiffuseRadiation() { return diffuseRadiation; }
    public void setDiffuseRadiation(BigDecimal diffuseRadiation) { this.diffuseRadiation = diffuseRadiation; }
    public Integer getSunshineDuration() { return sunshineDuration; }
    public void setSunshineDuration(Integer sunshineDuration) { this.sunshineDuration = sunshineDuration; }
    public Integer getWindLevel() { return windLevel; }
    public void setWindLevel(Integer windLevel) { this.windLevel = windLevel; }
    public BigDecimal getWindDirection() { return windDirection; }
    public void setWindDirection(BigDecimal windDirection) { this.windDirection = windDirection; }
    public BigDecimal getWindGusts() { return windGusts; }
    public void setWindGusts(BigDecimal windGusts) { this.windGusts = windGusts; }
    public BigDecimal getWindSpeed() { return windSpeed; }
    public void setWindSpeed(BigDecimal windSpeed) { this.windSpeed = windSpeed; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}