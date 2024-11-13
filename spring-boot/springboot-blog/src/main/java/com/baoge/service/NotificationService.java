package com.baoge.service;


import com.baoge.entity.Notification;
import com.baoge.utils.JsonResponse;

/**
 * @author wangxing
 * @description 针对表【notification(消息公告)】的数据库操作Service
 * @createDate 2024-11-07 10:27:17
 */
public interface NotificationService extends PageHelperService<Notification> {
    JsonResponse select(Notification req);
}
