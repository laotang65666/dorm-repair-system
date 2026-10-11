package com.dormrepair.pojo;

import lombok.Data;

/**
 * 报修单分页查询条件。
 * GET /repair/list?page=1&pageSize=10&userId=1&status=0
 * 哪个条件有值就拼哪个 WHERE，都没值就查全部。
 */
@Data
public class RepairOrderQuery {
    private Integer page = 1;       // 页码（从 1 开始）
    private Integer pageSize = 10;  // 每页记录数
    private Long userId;            // 按报修人过滤（学生查自己的）
    private Long workerId;          // 按维修员过滤（维修员查任务）
    private Integer status;         // 按状态过滤
    private String keyword;         // 关键词：模糊匹配报修单编号 / 标题
    private Long categoryId;        // 按报修类别过滤
    private String userName;        // 学生姓名（模糊匹配）
    private String workerName;      // 维修员姓名（模糊匹配）

    /** 计算 LIMIT 起始位置（(page-1)*pageSize），供 XML 分页用 */
    public int getStart() {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        return (p - 1) * s;
    }
}
