package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修单实体类 —— 对应 repair_order 表（核心表）
 *
 * 除表字段外，还包含：
 *   1. 展示字段（列表/详情 JOIN 出来）：categoryName / dormitoryName / userName / workerName
 *   2. 嵌套数据（详情查询出来）：progressList 进度列表、evaluation 评价
 *
 * 状态 status：0待派单 / 1已派单 / 2维修中 / 3已完成 / 4已评价 / 5已取消
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrder {
    private Long id;               // 报修单ID
    private String orderNo;        // 报修单编号（如 BX20260830123456）
    private Long userId;           // 报修人ID（学生）
    private Long categoryId;       // 报修类别ID
    private Long dormitoryId;      // 宿舍ID
    private String title;          // 报修标题
    private String description;    // 报修描述
    private String imageUrls;      // 图片地址（多图逗号分隔）
    private Long workerId;         // 维修员ID（派单后填充）
    private Integer status;        // 状态
    private LocalDateTime createTime; // 报修时间
    private LocalDateTime assignTime; // 派单时间
    private LocalDateTime finishTime; // 完成时间

    // ---- 展示字段（SQL JOIN 出来的名称，不落库）----
    private String categoryName;   // 类别名称
    private String dormitoryName;  // 宿舍名称（楼栋号+房间号）
    private String userName;       // 报修人姓名
    private String workerName;     // 维修员姓名

    // ---- 嵌套数据（详情接口填充）----
    private List<RepairProgress> progressList; // 维修进度列表
    private Evaluation evaluation;             // 评价
}
