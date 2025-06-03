package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.Student;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生服务接口
 */
public interface StudentService extends IService<Student> {
    /**
     * 分页查询学生列表，支持按姓名和证件号筛选
     * @param current 当前页
     * @param size 每页数量
     * @param name 姓名（可选）
     * @param identityDocumentNumber 证件号（可选）
     * @return 学生分页数据
     */
    Page<Student> listStudents(Integer current, Integer size, String name, String identityDocumentNumber);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);

    /**
     * 学生注册（带验证码验证）
     * @param student 学生信息
     * @param verificationCode 验证码
     */
    void registerWithVerificationCode(Student student, String verificationCode);

    /**
     * 学生登录
     * @param identityDocumentNumber 证件号码
     * @param password 密码
     * @return 学生信息
     */
    Student login(String identityDocumentNumber, String password);

    /**
     * 发送重置密码邮件
     * @param email 邮箱
     */
    void sendResetEmail(String email);

    /**
     * 重置密码
     * @param email 邮箱
     * @param code 验证码
     * @param newPassword 新密码
     */
    void resetPassword(String email, String code, String newPassword);
}