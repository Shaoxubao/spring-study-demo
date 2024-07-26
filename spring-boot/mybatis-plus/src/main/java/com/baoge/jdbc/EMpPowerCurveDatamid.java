package com.baoge.jdbc;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class EMpPowerCurveDatamid implements Serializable {
    private static final long serialVersionUID = 730188921857167759L;

    /**
     * 表id
     */
    private Long tableid;

    /**
     * 计量点ID
     */
    private Long id;
    /**
     * 数据日期，格式“20220701”
     */
    private String date;
    /**
     * 数据类型，整型，1-有功功率，2-A相有功功率，3-B相有功功率，4-C相有功功率，5-无功功率，6-A相无功功率，7-B相无功功率，8-C相无功功率
     */
    private Integer type;
    /**
     * 数据密度：5分钟，15分钟，30分钟，60分钟
     */
    private Integer density;
    /**
     * 开始分钟：15、30、45、60、75、......
     */
    private Integer start;
    /**
     * 数据点数
     */
    private Integer points;
    /**
     * 数据完整性标志,多个1或者0表示DATA的完整性
     */
    private String whole;
    /**
     * 数据
     */
    private String data;
    /**
     * 写入时间，格式“2019-01-01 00:00:00”
     */
    private String write;
    /**
     * 电能表ID
     */
    private Long meter;
    /**
     * 供电单位
     */
    private String org;

    private Long mpId;
}

