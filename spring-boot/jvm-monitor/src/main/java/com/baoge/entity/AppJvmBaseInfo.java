package com.baoge.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应用jvm基本信息
 * </p>
 *
 * @author baoge
 * @since 2024-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AppJvmBaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系统名
     */
    private String appName;

    /**
     * cup核数
     */
    private Integer cpuCount;

    /**
     * 磁盘总大小
     */
    private String diskSize;


}
