package com.baoge.mapper;

import com.baoge.model.AcctScheduleJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 82433
 * @description 针对表【acct_schedule_job_log】的数据库操作Mapper
 * @createDate 2023-02-23 14:47:55
 * @Entity generator.domain.AcctScheduleJobLog
 */
@Mapper
public interface AcctScheduleJobLogMapper {
    /**
     * 新增
     *
     * @param param
     */
    void insertAcctScheduleJobLog(@Param("param") AcctScheduleJobLog param);

    /**
     * 查询
     *
     * @return
     */
    List<AcctScheduleJobLog> select();

    /**
     * 修改
     *
     * @param acctScheduleJobLog
     */
    void updateAcctScheduleJobLogById(AcctScheduleJobLog acctScheduleJobLog);

    /**
     * 删除
     *
     * @return
     */
    void deleteById(@Param("id") String id);

}
