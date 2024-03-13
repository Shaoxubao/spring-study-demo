package com.baoge.mapper;

import com.baoge.entity.ConsCurve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ConsCurve10Mapper extends BaseMapper<ConsCurve> {

    @Select("select * from cons_curve where CONS_NO = '${consNo}' and DATA_DATE = '${date}'")
    ConsCurve find(@Param("consNo") String consNo, @Param("date") String date);
}
