package com.baoge.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 资源申报信息
 *
 */
@Data
public class ResDeclareVo implements Serializable {
    private static final long serialVersionUID = -1881382847853152595L;
    /**
     * 资源唯一标识
     */
    private String resourceId;

    /**
     * 资源名称：（举例：**园区、**充电站）
     */
    private String resourceName;

    /**
     * 资源编号
     */
    private String resourceNo;

    /**
     * 资源类型：分布式电源/可调负荷/储能/源网荷储一体化/虚拟类/其他
     */
    private String resourceType;

    /**
     * 所属线路
     */
    private String lineName;

    /**
     * 所属线路编号
     */
    private String lineNo;

    /**
     * 响应类型：01顶峰、02调峰、03储能充电、04储能放电
     */
    private String respType;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 持续时间
     */
    private String continueTime;

    /**
     * 响应时段
     */
    private String respTime;

    /**
     * 执行日期
     */
    private String executeTime;

    /**
     * 申报日期
     */
    private String declareTime;

    /**
     * 申报负荷曲线
     */
    private LoadCurveVo loadcurve;

    /**
     * 申报基线曲线
     */
    private BaseCurveVo basecurve;

    /**
     * 市场类型: 1 现货
     */
    private Integer marketType;

    /**
     * 套餐类型: 1 套餐
     */
    private Integer packageType;
    
}