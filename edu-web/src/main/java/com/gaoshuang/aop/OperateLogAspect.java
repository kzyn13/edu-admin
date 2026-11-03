package com.gaoshuang.aop;


import com.gaoshuang.mapper.OperateLogMapper;
import com.gaoshuang.pojo.OperateLog;
import com.gaoshuang.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面
 * 负责拦截 controller 包下的增删改方法并记录日志
 */
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * 定义切点：拦截 com.gaoshuang.controller 包及子包下所有方法
     * 实际项目中建议配合自定义注解 @OperateLog 使用，此处按题目要求直接匹配包路径
     */
//    @Pointcut("execution(* com.gaoshuang.controller..*.*(..))")
//    public void logPointCut() {}

    /**
     * 环绕通知：记录方法执行前后的关键信息
     */
    @Around("@annotation(com.gaoshuang.anno.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        long costTime = System.currentTimeMillis() - beginTime;

        // 组装日志对象
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(getCurrentUserId());//获取用户id
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        
        // 将参数和返回值序列化为 JSON 字符串，方便后续查看
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        operateLog.setReturnValue(result != null ? result.toString() : "void");
        operateLog.setCostTime(costTime);

        // 异步落库，避免阻塞主业务线程
        saveOperateLogAsync(operateLog);

        return result;
    }

    /**
     * 异步保存日志（核心优化点）
     * 日志记录属于非核心链路，使用 @Async 交由独立线程池执行，提升接口响应速度
     */
    @Async
    public void saveOperateLogAsync(OperateLog olog) {
        try {

            operateLogMapper.insert(olog);
            log.info("记录为{}",olog);
        } catch (Exception e) {
            log.info("操作日志保存失败", e);

        }
    }

    /**
     * 获取当前操作人ID
     * ⚠️ 此处为通用示例，实际项目请根据你的认证体系替换（如 Spring Security、Sa-Token、JWT 等）
     */
    private Integer getCurrentUserId() {
//        try {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            if (attributes != null) {
//                HttpServletRequest request = attributes.getRequest();
//                // 示例：从请求头获取用户ID，或从 Session/SecurityContext 获取
//                // String userId = request.getHeader("X-User-Id");
//                // Object sessionUser = request.getSession().getAttribute("userId");
//                // return Integer.parseInt(userId);
//                return 1; // 临时模拟值，请替换为真实获取逻辑
//            }
//        } catch (Exception e) {
//            log.warn("获取当前操作用户ID失败，原因：{}", e.getMessage());
//        }
        return CurrentHolder.getCurrentId();
    }
}
