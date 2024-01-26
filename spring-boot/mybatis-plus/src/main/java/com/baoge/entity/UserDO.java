package com.baoge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;


@TableName("t_user")
@Data
public class UserDO implements Serializable {

    @TableField()
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String username;

//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private String phone;

//    @TableField(insertStrategy = FieldStrategy.NOT_NULL, fill = FieldFill.INSERT)
//    private LocalDateTime createTime;

//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private Integer status;

}