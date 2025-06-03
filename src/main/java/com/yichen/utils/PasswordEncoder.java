package com.yichen.utils;

import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 */
@Component
public class PasswordEncoder {
    
    private static final String SALT = "yichen_parking_system";
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(String rawPassword) {
        // 使用MD5加盐加密
        return DigestUtil.md5Hex(rawPassword + SALT);
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
} 