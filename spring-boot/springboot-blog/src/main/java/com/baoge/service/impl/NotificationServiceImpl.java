package com.baoge.service.impl;


import com.baoge.entity.Notification;
import com.baoge.mapper.NotificationMapper;
import com.baoge.service.NotificationService;
import com.baoge.utils.JsonResponse;
import com.baoge.utils.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
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
        Page page = PageMethod.startPage(req.getPageNum(), req.getPageSize());

        List list = notificationMapper.select(req);
        PageInfo info = PageUtil.getResult(page, list, Object.class);
        return JsonResponse.success(info);
    }
}




