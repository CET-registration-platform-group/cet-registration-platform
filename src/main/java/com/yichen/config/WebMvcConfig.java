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
    
//    private final AuthInterceptor authInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册认证拦截器，拦截所有请求
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/**");
//    }
} 