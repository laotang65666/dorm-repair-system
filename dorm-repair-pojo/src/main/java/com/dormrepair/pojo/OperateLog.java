package com.dormrepair.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志实体类 —— 对应 operate_log 表
 *
 * 由 OperationLogAspect 切面自动写入，记录后台的关键操作（增删改等）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    private Long id;               // 日志ID
    private Long operateUserId;    // 操作人ID
    private LocalDateTime operateTime; // 操作时间
    private String className;      // 被调用的类名
    private String methodName;     // 被调用的方法名
    private String methodParams;   // 方法参数
    private String returnValue;    // 方法返回值
    private Long costTime;         // 耗时（毫秒）
}
