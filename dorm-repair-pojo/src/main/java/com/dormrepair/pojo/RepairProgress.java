package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 维修进度实体类 —— 对应 repair_progress 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairProgress {
    private Long id;           // 进度ID
    private Long orderId;      // 所属报修单ID
    private String content;    // 进度内容（如 "已上门维修"）
    private LocalDateTime createTime; // 记录时间
}
