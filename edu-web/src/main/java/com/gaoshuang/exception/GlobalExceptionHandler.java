package com.gaoshuang.exception;

import com.gaoshuang.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * <p>
 * 分层处理：业务异常返回具体提示，其他异常只返回通用提示，
 * 真实堆栈记录到日志，避免把 SQL、文件路径等内部信息暴露给前端。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常：可以把具体原因返回给前端 */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException ex) {
        log.warn("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /** 兜底异常：只返回通用提示，详细信息记录到日志 */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error("系统异常", ex);
        return Result.error("服务器内部错误");
    }
}
