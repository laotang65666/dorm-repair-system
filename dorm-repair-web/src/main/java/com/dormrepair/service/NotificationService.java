package com.dormrepair.service;

import com.dormrepair.pojo.Notification;

import java.util.List;

/**
 * 通知业务逻辑接口
 */
public interface NotificationService {

    /** 发送一条通知（给指定用户） */
    void createNotification(Long userId, String title, String content);

    /** 查询某用户的通知列表 */
    List<Notification> listByUser(Long userId);

    /** 某用户未读通知数 */
    Long countUnread(Long userId);

    /** 标记某条通知为已读 */
    void markRead(Long userId, Long id);

    /** 标记某用户全部通知为已读 */
    void markAllRead(Long userId);
}
