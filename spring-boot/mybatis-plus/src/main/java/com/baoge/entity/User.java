package com.baoge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 */
@Getter
@Setter
@Accessors(chain = true)


@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基于雪花算法生成id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别：0-女 1-男
     */
    private Integer sex;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedDate;

    /**
     * 删除标识
     */
    @TableLogic
    private Integer isDeleted;


}
