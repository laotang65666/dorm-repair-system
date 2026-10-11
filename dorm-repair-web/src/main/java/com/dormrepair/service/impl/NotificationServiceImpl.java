package com.dormrepair.service.impl;

import com.dormrepair.mapper.NotificationMapper;
import com.dormrepair.pojo.Notification;
import com.dormrepair.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void createNotification(Long userId, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);   // 默认未读
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);
    }

    @Override
    public List<Notification> listByUser(Long userId) {
        return notificationMapper.listByUserId(userId);
    }

    @Override
    public Long countUnread(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public void markRead(Long userId, Long id) {
        notificationMapper.markRead(userId, id);
    }

    @Override
    public void markAllRead(Long userId) {
        notificationMapper.markAllRead(userId);
    }
}
