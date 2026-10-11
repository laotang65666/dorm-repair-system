package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 用户实体类 —— 对应数据库的 sys_user 表
 *
 * 角色 role：student 学生 / worker 维修员 / admin 管理员
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;               // 用户ID
    private String username;       // 用户名
    @ToString.Exclude             // 密码不参与 toString，避免被 @Log 切面打印进日志
    private String password;       // 密码（BCrypt 加密）
    private String realName;       // 姓名
    private String role;           // 角色
    private String gender;         // 性别：男 / 女
    private String phone;          // 手机号
    private Long dormitoryId;      // 所在宿舍ID（仅学生）
    private Integer status;        // 状态：1启用 / 0禁用
    private LocalDateTime createTime; // 创建时间
}
