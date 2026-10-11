package com.dormrepair.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色权限注解：标记在 Controller 方法上，声明「这个接口需要什么角色才能访问」。
 * 例如 @RequireRole("admin") 表示只有管理员能调。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String[] value();
}
