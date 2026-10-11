package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知实体类 —— 对应 notification 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;           // 通知ID
    private Long userId;       // 接收人ID
    private String title;      // 通知标题
    private String content;    // 通知内容
    private Integer isRead;    // 是否已读：1是 / 0否
    private LocalDateTime createTime; // 通知时间
}
