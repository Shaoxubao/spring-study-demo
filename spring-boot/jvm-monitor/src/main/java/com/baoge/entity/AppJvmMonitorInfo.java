package com.baoge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用jvm监控信息
 * </p>
 *
 * @author baoge
 * @since 2024-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppJvmMonitorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统名
     */
    private String appName;

    /**
     * 数据时间
     */
    private Date dataDate;

    /**
     * cpu使用率
     */
    private String cpuLoad;

    /**
     * 系统总内存
     */
    private String totalMemory;

    /**
     * 系统空闲内存
     */
    private String freeMemory;

    /**
     * 系统使用中内存
     */
    private String useMemory;

    /**
     * 创建时间
     */
    private Date createTime;


}
