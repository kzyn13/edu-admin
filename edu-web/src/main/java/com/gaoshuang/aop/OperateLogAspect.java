package com.gaoshuang.aop;

import com.gaoshuang.mapper.OperateLogMapper;
import com.gaoshuang.pojo.OperateLog;
import com.gaoshuang.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面
 * <p>
 * 切点用 {@code @annotation} 精确匹配「标注了 @Log 的方法」，
 * 只增强需要的接口，而不是一刀切拦截整个 controller 包。
 * <p>
 * 为什么用 {@code @Around} 而不是 {@code @Before}：
 * 需要计算耗时（方法前后各记一次时间）、需要拿到返回值、
 * 并且要保证日志在方法执行之后才落库。
 */
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.gaoshuang.anno.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();

        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(CurrentHolder.getCurrentId());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            operateLog.setReturnValue(result == null ? "void" : result.toString());
            return result;
        } catch (Throwable e) {
            // 审计日志最需要记录的恰恰是【失败的操作】：
            // 出了事要能查到「谁在什么时候尝试做了什么、失败原因是什么」
            operateLog.setReturnValue("异常: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            throw e;              // 异常必须继续抛出，不能被切面吞掉
        } finally {
            // 无论成功还是失败都要记录
            operateLog.setCostTime(System.currentTimeMillis() - beginTime);
            saveOperateLog(operateLog);
        }
    }

    /**
     * 保存操作日志
     * <p>
     * 这里是<b>同步</b>落库。日志属于非核心链路，同步写能保证不丢；
     * 如果后续 QPS 上来，可以改成发 MQ 异步处理。
     * <p>
     * 说明：这里不能用 {@code @Async}——它被本类内部方法调用，
     * {@code this.} 调用走不到代理，异步不生效；
     * 而且项目里也没有开启 {@code @EnableAsync}
     * （事务是 Spring Boot 自动开的，但异步必须手动加开关）。
     */
    private void saveOperateLog(OperateLog operateLog) {
        try {
            operateLogMapper.insert(operateLog);
        } catch (Exception e) {
            // 日志写入失败不能影响主业务，只记录错误
            log.error("操作日志保存失败", e);
        }
    }
}
