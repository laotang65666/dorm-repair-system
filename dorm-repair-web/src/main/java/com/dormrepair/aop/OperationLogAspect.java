package com.dormrepair.aop;

import com.dormrepair.mapper.OperateLogMapper;
import com.dormrepair.pojo.OperateLog;
import com.dormrepair.pojo.User;
import com.dormrepair.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面。
 *
 * AOP（面向切面编程）的典型用法：把「记录操作日志」这种每个方法都要做的横切逻辑，
 * 从业务代码里抽出来，集中到一个切面里。业务方法只要贴个 @Log 注解，日志就自动记录了。
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.dormrepair.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 执行目标方法（真正的业务逻辑）
        Object result = joinPoint.proceed();

        // 算耗时
        long cost = System.currentTimeMillis() - start;

        // 组装操作日志实体
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateUserId(getCurrentUserId());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        operateLog.setReturnValue(String.valueOf(result));
        operateLog.setCostTime(cost);

        // 写入日志表
        operateLogMapper.insert(operateLog);

        // 把目标方法的返回值原样返回，不影响正常业务
        return result;
    }

    /** 从 UserContext 取当前登录用户 id */
    private Long getCurrentUserId() {
        User user = UserContext.get();
        return user == null ? null : user.getId();
    }
}
