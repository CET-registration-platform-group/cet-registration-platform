package com.yichen.utils;

import com.yichen.exception.ConstraintViolationException;

/**
 * 业务规则校验工具类
 * 用于检查业务规则约束，抛出明确的约束违反异常
 */
public class BusinessValidationUtil {
    
    /**
     * 检查条件是否为真，如果为假则抛出约束违反异常
     * @param condition 条件
     * @param message 错误信息
     */
    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new ConstraintViolationException(message);
        }
    }
    
    /**
     * 检查对象是否为null，如果为null则抛出约束违反异常
     * @param object 待检查对象
     * @param message 错误信息
     */
    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new ConstraintViolationException(message);
        }
    }
    
    /**
     * 检查字符串是否为空，如果为空则抛出约束违反异常
     * @param str 待检查字符串
     * @param message 错误信息
     */
    public static void checkNotEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new ConstraintViolationException(message);
        }
    }
    
    /**
     * 检查对象是否相等，如果不相等则抛出约束违反异常
     * @param obj1 对象1
     * @param obj2 对象2
     * @param message 错误信息
     */
    public static void checkEquals(Object obj1, Object obj2, String message) {
        if (obj1 == null ? obj2 != null : !obj1.equals(obj2)) {
            throw new ConstraintViolationException(message);
        }
    }
} 