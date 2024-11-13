package com.baoge.mapper;

import com.baoge.entity.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author wangxing
 * @description 针对表【notification(消息公告)】的数据库操作Mapper
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    List<Notification> select(Notification req);
}




