package com.baoge.mapper;

import com.baoge.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where account = #{account}")
    User findByAccount(@Param("account") String account);
}