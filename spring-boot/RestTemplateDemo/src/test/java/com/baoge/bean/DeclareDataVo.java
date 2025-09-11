package com.baoge.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeclareDataVo implements Serializable {
    private static final long serialVersionUID = -6985240274164587191L;
    /**
     * 申报信息标识
     */
    private String plantDeclareId;

    /**
     * 虚拟电厂唯一标识（资格预审：虚拟电厂编号）
     */
    private String plantId;

    /**
     * 虚拟电厂编号
     */
    private String plantNo;

    /**
     * 虚拟电厂名称（举例：全省空调负荷管理平台）
     */
    private String plantName;

    /**
     * 虚拟电厂类型
     */
    private String plantType;

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
     * 执行日期
     */
    private String executeTime;

    /**
     * 申报日期
     */
    private String declareTime;

    /**
     * 资源申报信息
     */
    List<ResDeclareVo> resDeclare;

    /**
     * 用户申报信息
     */
    List<ConsDeclareVo> consDeclare;

    /**
     * 市场类型: 1 现货
     */
    private Integer marketType;

    /**
     * 套餐类型: 1 套餐
     */
    private Integer packageType;

}
