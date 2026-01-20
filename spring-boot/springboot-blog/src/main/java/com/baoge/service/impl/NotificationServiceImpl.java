package com.baoge.service.impl;

import com.baoge.entity.Notification;
import com.baoge.mapper.NotificationMapper;
import com.baoge.service.NotificationService;
import com.baoge.utils.JsonResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author wangxing
 * @description 针对表【notification(消息公告)】的数据库操作Service实现
 * @createDate 2024-11-07 10:27:17
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends PageHelperServiceImpl<NotificationMapper, Notification>
        implements NotificationService {

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    public JsonResponse select(Notification req) {
        int pageNum = Optional.ofNullable(req.getPageNum()).orElse(DEFAULT_PAGE_NUM);
        int pageSize = Optional.ofNullable(req.getPageSize()).orElse(DEFAULT_PAGE_SIZE);
        
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Notification> pageInfo = new PageInfo<>(baseMapper.select(req));
        return JsonResponse.success(pageInfo);
    }
}




