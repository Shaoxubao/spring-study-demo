package com.baoge.controller;

import com.baoge.entity.Notification;
import com.baoge.service.NotificationService;
import com.baoge.utils.JsonResponse;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Description: 消息公告（notification）
 */
@RestController
@RequestMapping("/notification")
@Slf4j
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    /**
     * 分页列表查询
     */
    @CrossOrigin
    @PostMapping(value = "/list")
    public JsonResponse<PageInfo<Notification>> queryPageList(@RequestBody Notification req) {
        return notificationService.select(req);
    }

    /**
     * 添加
     */
    @CrossOrigin
    @PostMapping(value = "/add")
    public JsonResponse<String> add(@RequestBody Notification req) {
        req.setId(UUID.randomUUID().toString().replace("-", ""));
        notificationService.save(req);
        return JsonResponse.success("添加成功！");
    }

    /**
     * 编辑
     */
    @CrossOrigin
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public JsonResponse<String> edit(@RequestBody Notification req) {
        notificationService.updateById(req);
        return JsonResponse.success("编辑成功!");
    }

    /**
     * 通过id删除
     */
    @CrossOrigin
    @PostMapping(value = "/delete")
    public JsonResponse<String> delete(@RequestBody Notification req) {
        notificationService.removeById(req.getId());
        return JsonResponse.success("删除成功!");
    }

    /**
     * 通过id查询
     */
    @CrossOrigin
    @PostMapping(value = "/queryById")
    public JsonResponse<Notification> queryById(@RequestBody Notification req) {
        Notification entity = notificationService.getById(req.getId());
        if (entity == null) {
            return JsonResponse.fail("未找到对应数据");
        }
        return JsonResponse.success(entity);
    }

}
