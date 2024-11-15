package com.baoge.service.impl;


import com.baoge.entity.Notification;
import com.baoge.mapper.NotificationMapper;
import com.baoge.service.NotificationService;
import com.baoge.utils.JsonResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangxing
 * @description 针对表【notification(消息公告)】的数据库操作Service实现
 * @createDate 2024-11-07 10:27:17
 */
@Service
public class NotificationServiceImpl extends PageHelperServiceImpl<NotificationMapper, Notification>
        implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public JsonResponse select(Notification req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<Notification> list = notificationMapper.select(req);
        PageInfo<Notification> pageInfo = new PageInfo<>(list);
        return JsonResponse.success(pageInfo);
    }
}




