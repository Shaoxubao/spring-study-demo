package com.baoge.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class AcctScheduleJob {

    @TableId(value = "id")
    private Integer id;

    /**
     * 任务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 任务状态 1有效 2无效
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 任务时间表达式
     */
    @TableField(value = "corn")
    private String corn;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 包名+类名
     */
    @TableField(value = "bean_class")
    private String beanClass;

    /**
     * 方法名称
     */
    @TableField(value = "method_name")
    private String methodName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_name")
    private String createName;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "update_name")
    private String updateName;

    /**
     * 分组code
     */
    @TableField(value = "group_code")
    private String groupCode;

    /**
     * 分组查询code
     */
    @TableField(value = "group_query_code")
    private String groupQueryCode;

    /**
     * sqlID
     */
    @TableField(value = "sql_id")
    private Integer sqlId;


}
