package com.dormrepair.exception;

import com.dormrepair.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：统一所有接口的错误返回格式。
 * 好处：
 *   1. 前端拿到的错误永远是 {code:0, msg:xxx}，不用猜格式；
 *   2. 详细的异常信息只记录在服务器日志里，不泄露给前端（安全）。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常：直接返回给用户看的提示（如「密码错误」「状态不允许」） */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.error(e.getMessage());
    }

    /** 唯一约束冲突（如用户名重复、报修单编号重复） */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据重复异常", e);
        return Result.error("数据已存在，请勿重复提交");
    }

    /** 外键/数据完整性冲突（如删除仍被引用的类别、宿舍） */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("数据完整性异常", e);
        return Result.error("存在关联数据，无法删除");
    }

    /** 其他所有异常：记录详细日志，返回通用错误（不暴露内部细节） */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("服务器内部错误");
    }
}
