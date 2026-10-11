package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 宿舍实体类 —— 对应 dormitory 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dormitory {
    private Long id;           // 宿舍ID
    private String buildingNo; // 楼栋号（如 "1号楼"）
    private String roomNo;     // 房间号（如 "101"）
    private Integer floor;     // 楼层
}
