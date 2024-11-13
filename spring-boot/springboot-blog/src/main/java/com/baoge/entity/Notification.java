package com.baoge.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 消息公告
 *
 * @TableName notification
 */
@TableName(value = "notification")
@Data
public class Notification extends BaseEntity {
    /**
     * 唯一标识
     */
    @TableId
    private String id;

    /**
     * 一级标题
     */
    private String primaryTitle;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 公开范围
     */
    private String publicScope;

    /**
     * 发布来源
     */
    private String publishSource;

    /**
     * 发布时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 一级分类
     */
    private String firstClass;

    /**
     * 二级分类
     */
    private String secondClass;

    /**
     * 附件
     */
    private String fliePath;

    /**
     * 创建人
     */
    private String createUserId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 附件
     */
    @TableField(exist = false)
    private List<Long> attrIds;
}
