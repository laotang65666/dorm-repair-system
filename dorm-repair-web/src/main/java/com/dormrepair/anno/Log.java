package com.dormrepair.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解。
 * 标记在 Controller 方法上，声明「这个方法需要自动记录操作日志」。
 * 配合 OperationLogAspect 切面使用：方法执行完，切面自动把
 * 「谁、什么时候、调了哪个方法、参数、结果、耗时」写入 operate_log 表。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
