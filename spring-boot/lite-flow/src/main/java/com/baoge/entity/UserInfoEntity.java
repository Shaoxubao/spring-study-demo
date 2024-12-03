package com.baoge.entity;

import com.baoge.constants.SexEnum;
import lombok.Data;

@Data
public class UserInfoEntity {
    private String name;
    private SexEnum sex;
}