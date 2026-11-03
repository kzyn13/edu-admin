package com.gaoshuang.filter;

import com.gaoshuang.utils.CurrentHolder;
import com.gaoshuang.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 登录校验过滤器
 * <p>
 * 解析请求头中的 JWT，把员工 id 存入 ThreadLocal，
 * 供后续业务代码和 AOP 切面读取。
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 登录接口直接放行
        if (request.getRequestURI().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            log.info("请求头 token 为空");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Integer empId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            empId = Integer.valueOf(claims.get("id").toString());
        } catch (Exception e) {
            log.info("token 非法：{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 必须在 finally 中清理：Tomcat 复用线程，
        // 不清理不仅会内存泄漏，下一个请求还可能读到上一个用户的 id
        try {
            CurrentHolder.setCurrentId(empId);
            filterChain.doFilter(request, response);
        } finally {
            CurrentHolder.remove();
        }
    }
}
