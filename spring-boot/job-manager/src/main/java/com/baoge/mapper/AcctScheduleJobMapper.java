package com.baoge.mapper;


import com.baoge.model.AcctScheduleJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface AcctScheduleJobMapper {


    /**
     * 获取状态=1的任务
     *
     * @return
     */
    List<AcctScheduleJob> getInitJob(@Param("status") Integer status, @Param("taskSort") Integer taskSort);

    /**
     * 获取指定的的定时任务
     *
     * @return
     */
    AcctScheduleJob getJobByBeanClass(String beanClass);

    void updateJobStatus(@Param("status") Integer status, @Param("id") Integer id);


}

