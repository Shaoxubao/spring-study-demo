package com.baoge.mapper;

import com.baoge.model.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper {
    @Select("select * from client where account = #{account}")
    Client findByAccount(@Param("account") String account);
}