package com.dormrepair.config;

import com.dormrepair.anno.RequireRole;
import com.dormrepair.common.TokenService;
import com.dormrepair.pojo.User;
import com.dormrepair.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 认证拦截器：在请求到达 Controller 之前，先验证 token 和权限。
 *
 * 规则：
 *   1. 所有接口都要求登录（登录/注册已在 WebConfig 里排除）；
 *   2. 方法上标了 @RequireRole 的，还要检查角色。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理 Controller 的方法（静态资源等不是 HandlerMethod，直接放行）
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        // 第 1 步：从请求头取 token，还原出用户
        String token = request.getHeader("Authorization");
        User user = tokenService.getUser(token);

        // 第 2 步：所有接口都要求登录（登录/注册已排除）
        if (user == null) {
            writeJson(response, 401, "未登录或登录已过期");
            return false;
        }
        UserContext.set(user);

        // 第 3 步：看方法上有没有 @RequireRole 注解
        RequireRole requireRole = method.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            return true; // 没要求角色，登录即可访问
        }

        // 第 4 步：检查用户角色是否在允许列表中
        for (String role : requireRole.value()) {
            if (role.equals(user.getRole())) {
                return true;
            }
        }

        // 角色不匹配，拒绝
        writeJson(response, 403, "没有权限");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后，清除 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }

    /** 往响应里写一段 JSON 错误信息 */
    private void writeJson(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":0,\"msg\":\"" + message + "\"}");
    }
}
