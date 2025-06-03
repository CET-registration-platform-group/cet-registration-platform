package com.yichen.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.nickname}")
    private String mailNickname;

    // 存储邮箱验证码，key为邮箱，value为验证码
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    // 存储验证码过期时间，key为邮箱，value为过期时间戳
    private final Map<String, Long> codeExpiration = new ConcurrentHashMap<>();
    // 验证码有效期（毫秒）
    private static final long CODE_VALIDITY_PERIOD = 10 * 60 * 1000; // 10分钟

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
        message.setSubject(mailNickname + " - 邮箱验证");
        message.setText("您好，\n\n您的验证码是：" + code + "，有效期为10分钟。\n\n如果这不是您的操作，请忽略此邮件。\n\n" + mailNickname);
        mailSender.send(message);

        // 保存验证码和过期时间
        verificationCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + CODE_VALIDITY_PERIOD);
    }

    /**
     * 发送重置密码邮件
     */
    public void sendResetPasswordEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(mailNickname + " - 重置密码");
        message.setText("您好，\n\n您的验证码是：" + code + "，有效期为10分钟。\n\n如果这不是您的操作，请忽略此邮件。\n\n" + mailNickname);
        mailSender.send(message);

        // 保存验证码和过期时间
        verificationCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + CODE_VALIDITY_PERIOD);
    }

    /**
     * 验证验证码
     */
    public boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        Long expirationTime = codeExpiration.get(email);

        if (savedCode == null || expirationTime == null) {
            return false;
        }

        // 检查验证码是否过期
        if (System.currentTimeMillis() > expirationTime) {
            verificationCodes.remove(email);
            codeExpiration.remove(email);
            return false;
        }

        // 验证码正确，使用后删除
        if (savedCode.equals(code)) {
            verificationCodes.remove(email);
            codeExpiration.remove(email);
            return true;
        }

        return false;
    }
    
    /**
     * 保存验证码
     */
    public void saveVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + CODE_VALIDITY_PERIOD);
    }
    
    /**
     * 删除验证码
     */
    public void removeVerificationCode(String email) {
        verificationCodes.remove(email);
        codeExpiration.remove(email);
    }
} 