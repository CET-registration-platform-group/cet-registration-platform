package com.yichen.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.properties.nickname}")
    private String mailNickname;

    @Value("${spring.mail.properties.from}")
    private String mailFrom;

    // 使用Caffeine缓存，10分钟过期时间
    private final Cache<String, String> verificationCodeCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    /**
     * 生成6位数字验证码
     */
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 发送验证邮件
     */
    public void sendVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(mailFrom);
        message.setSubject(mailNickname + " - 邮箱验证");
        message.setText("您好，\n\n您的验证码是：" + code + "，有效期为10分钟。\n\n如果这不是您的操作，请忽略此邮件。\n\n" + mailNickname);
        mailSender.send(message);

        // 保存验证码到缓存
        verificationCodeCache.put(email, code);
    }

    /**
     * 发送重置密码邮件
     */
    public void sendResetPasswordEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject(mailNickname + " - 重置密码");
        message.setText("您好，\n\n您的验证码是：" + code + "，有效期为10分钟。\n\n如果这不是您的操作，请忽略此邮件。\n\n" + mailNickname);
        mailSender.send(message);

        // 保存验证码到缓存
        verificationCodeCache.put(email, code);
    }

    /**
     * 验证验证码
     */
    public boolean verifyCode(String email, String code) {
        String savedCode = verificationCodeCache.getIfPresent(email);

        if (savedCode == null) {
            return false;
        }

        // 验证码正确，使用后删除
        if (savedCode.equals(code)) {
            verificationCodeCache.invalidate(email);
            return true;
        }

        return false;
    }
    
    /**
     * 保存验证码
     */
    public void saveVerificationCode(String email, String code) {
        verificationCodeCache.put(email, code);
    }
    
    /**
     * 删除验证码
     */
    public void removeVerificationCode(String email) {
        verificationCodeCache.invalidate(email);
    }
} 