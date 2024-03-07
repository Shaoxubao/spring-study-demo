package com.baoge.mapper;

import com.baoge.entity.ConsCurve10;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ConsCurve10Mapper extends BaseMapper<ConsCurve10> {

    @Select("select * from cons_curve10 where CONS_NO = '${consNo}' and DATA_DATE = '${date}'")
    ConsCurve10 find(@Param("consNo") String consNo, @Param("date") String date);
}
