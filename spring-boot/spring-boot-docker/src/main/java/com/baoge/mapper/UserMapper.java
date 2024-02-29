package com.baoge.mapper;

import com.baoge.bean.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User findUserByName(@Param("username") String username);
}
