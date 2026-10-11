package com.dormrepair.controller;

import com.dormrepair.pojo.Result;
import com.dormrepair.service.NotificationService;
import com.dormrepair.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知接口（当前登录用户自己的通知）。
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /** 查询当前用户的通知：GET /notification/list */
    @GetMapping("/list")
    public Result list() {
        return Result.success(notificationService.listByUser(currentUserId()));
    }

    /** 未读通知数：GET /notification/unreadCount */
    @GetMapping("/unreadCount")
    public Result unreadCount() {
        return Result.success(notificationService.countUnread(currentUserId()));
    }

    /** 标记已读：POST /notification/read?id= */
    @PostMapping("/read")
    public Result read(@RequestParam Long id) {
        notificationService.markRead(currentUserId(), id);
        return Result.success();
    }

    /** 全部标记已读：POST /notification/readAll */
    @PostMapping("/readAll")
    public Result readAll() {
        notificationService.markAllRead(currentUserId());
        return Result.success();
    }

    private Long currentUserId() {
        return UserContext.get().getId();
    }
}
