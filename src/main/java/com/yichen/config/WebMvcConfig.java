package com.yichen.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，注册拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/admin/**", "/api/student/**")
                .excludePathPatterns(
                    // 公共接口
                    "/api/admin/auth/login", 
                    "/api/admin/auth/logout",
                    // 前台特有的不需要登录的接口
                    "/api/student/register",
                    "/api/student/send-verification-code",
                    "/api/student/login",
                    "/api/student/send-reset-email",
                    "/api/student/reset-password",
                    // Swagger文档相关
                    "/doc.html", 
                    "/swagger-resources/**", 
                    "/v2/api-docs/**", 
                    "/webjars/**"
                );
    }
} 