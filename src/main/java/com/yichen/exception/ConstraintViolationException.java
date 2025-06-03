package com.yichen.exception;

/**
 * 自定义约束违反异常
 * 用于处理实体间的逻辑约束冲突
 */
public class ConstraintViolationException extends RuntimeException {
    
    /**
     * 默认构造函数
     */
    public ConstraintViolationException() {
        super();
    }
    
    /**
     * 带消息的构造函数
     * @param message 错误消息
     */
    public ConstraintViolationException(String message) {
        super(message);
    }
    
    /**
     * 带消息和原因的构造函数
     * @param message 错误消息
     * @param cause 原因
     */
    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
} 