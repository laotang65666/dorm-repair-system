package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报修类别实体类 —— 对应 repair_category 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairCategory {
    private Long id;          // 类别ID
    private String name;      // 类别名称（水电报修、门窗报修…）
    private String description; // 类别描述
}
