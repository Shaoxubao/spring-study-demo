package com.baoge.mapper;

import com.baoge.entity.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@DS("slave_1")
public interface TestDb2Mapper extends BaseMapper<User> {
}
