package com.yichen.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);

    @Around("execution(* com.yichen.controller..*.*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 打印请求开始信息
        logger.info("Method {} is starting with arguments: {}", methodName, args);

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 执行目标方法
        long endTime = System.currentTimeMillis();

        // 打印请求结束信息
        logger.info("Method {} executed in {} ms and returned: {}", methodName, (endTime - startTime), result);

        return result;
    }
}