package com.dormrepair.exception;

/**
 * 业务异常：业务规则校验失败时抛出（如密码错误、状态不允许、数据不存在等）。
 * 由 GlobalExceptionHandler 统一捕获，转成 {code:0, msg:...} 返回给前端。
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
