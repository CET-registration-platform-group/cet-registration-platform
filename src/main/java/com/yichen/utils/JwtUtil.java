package com.yichen.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类，用于生成、验证和解析JWT令牌
 */
@Component
public class JwtUtil {
    // 密钥，实际应用中应该放在配置文件中并加密
    private static final String SECRET_KEY = "yichen_cet_registration_system_jwt_secret_key_must_be_very_long_at_least_32_bytes";
    // token有效期（毫秒）
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时
    
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * 生成token
     * @param userId 学生ID
     * @param username 学生姓名
     * @return token字符串
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRE_TIME);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)  // 设置私有声明
                .setIssuedAt(now)   // 设置签发时间
                .setExpiration(expireDate)  // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256);  // 设置签名算法
        
        return builder.compact();
    }
    
    /**
     * 验证token是否有效
     * @param token token字符串
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从token中获取学生信息
     * @param token token字符串
     * @return 声明对象
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从token中获取学生ID
     * @param token token字符串
     * @return 学生ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? ((Number) claims.get("userId")).longValue() : null;
    }
    
    /**
     * 从token中获取学生姓名
     * @param token token字符串
     * @return 学生姓名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get("username") : null;
    }
} 