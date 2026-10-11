package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评价实体类 —— 对应 evaluation 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    private Long id;           // 评价ID
    private Long orderId;      // 被评价的报修单ID
    private Long userId;       // 评价人ID（学生）
    private Integer rating;    // 评分（1~5）
    private String content;    // 评价内容
    private LocalDateTime createTime; // 评价时间
}
