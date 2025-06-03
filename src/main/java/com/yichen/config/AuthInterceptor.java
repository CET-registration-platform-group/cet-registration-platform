package com.yichen.config;

import com.alibaba.fastjson.JSON;
import com.yichen.entity.Student;
import com.yichen.entity.User;
import com.yichen.mapper.StudentMapper;
import com.yichen.mapper.UserMapper;
import com.yichen.utils.JwtUtil;
import com.yichen.utils.UserContext;
import com.yichen.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证拦截器，拦截未登录用户访问需要认证的接口
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserContext userContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 获取请求URI
        String requestURI = request.getRequestURI();
        
        // 判断是前台还是后台请求
        boolean isAdminRequest = requestURI.startsWith("/api/admin/");
        
        // 获取Authorization请求头中的Token
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "未提供有效的认证信息")));
            return false;
        }
        
        // 提取Token并验证
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "认证信息已过期或无效")));
            return false;
        }
        
        // Token有效，获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "无效的用户信息")));
            return false;
        }
        
        // 根据请求类型验证不同的用户
        if (isAdminRequest) {
            // 后台请求，验证管理员
            User user = userMapper.selectById(userId);
            if (user == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSON.toJSONString(Result.error(401, "管理员不存在或已被删除")));
                return false;
            }
            userContext.setCurrentUser(user);
        } else {
            // 前台请求，验证学生
            Student student = studentMapper.selectById(userId);
            if (student == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSON.toJSONString(Result.error(401, "学生不存在或已被删除")));
                return false;
            }
            userContext.setCurrentStudent(student);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理上下文
        userContext.clear();
    }
} 