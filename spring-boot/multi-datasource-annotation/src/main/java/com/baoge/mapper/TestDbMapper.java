package com.baoge.mapper;

import com.baoge.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

// 没有@DS，使用默认数据源
public interface TestDbMapper extends BaseMapper<User> {
}
