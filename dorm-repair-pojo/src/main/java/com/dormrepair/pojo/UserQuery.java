package com.dormrepair.pojo;

import lombok.Data;

/**
 * 用户分页查询条件。
 * GET /user/page?page=1&pageSize=10&role=student
 */
@Data
public class UserQuery {
    private Integer page = 1;       // 页码（从 1 开始）
    private Integer pageSize = 10;  // 每页记录数
    private String role;            // 按角色过滤（可选）

    /** 计算 LIMIT 起始位置（(page-1)*pageSize），供 XML 分页用 */
    public int getStart() {
        int p = (page == null || page < 1) ? 1 : page;
        int s = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        return (p - 1) * s;
    }
}
