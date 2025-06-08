package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.common.Result;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.StudentMapper;
import com.yichen.entity.Student;
import com.yichen.service.ConstraintService;
import com.yichen.service.StudentService;
import com.yichen.utils.*;
import com.yichen.vo.StudentVO;
import com.yichen.vo.TokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生服务实现类
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final ConstraintService constraintService;
    private final JwtUtil jwtUtil;
    private final BeanConverter beanConverter;

    @Autowired
    private EmailUtils emailUtils;

    @Override
    public Page<Student> listStudents(Integer current, Integer size, String name, String identityDocumentNumber) {
        Page<Student> page = new Page<>(current, size);
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Student::getName, name);
        }

        if (identityDocumentNumber != null && !identityDocumentNumber.isEmpty()) {
            queryWrapper.like(Student::getIdentityDocumentNumber, identityDocumentNumber);
        }

        return getBaseMapper().selectPage(page, queryWrapper);
    }

    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Student entity) {
        // 获取原始实体
        Student original = getById(entity.getId());
        BusinessValidationUtil.checkNotNull(original, "学生不存在");

        // 检查证件号是否被修改且已被使用
        String identityDocumentNumber = entity.getIdentityDocumentNumber();
        String originalIdentityDocumentNumber = original.getIdentityDocumentNumber();
        if (identityDocumentNumber != null && !identityDocumentNumber.isEmpty()
                && !identityDocumentNumber.equals(originalIdentityDocumentNumber)) {
            // 检查新证件号是否唯一
            boolean isUnique = constraintService.isIdentityDocumentNumberUnique(identityDocumentNumber, entity.getId());
            BusinessValidationUtil.check(isUnique, "该证件号已被其他学生使用");

            // 如果证件号变更，检查原证件号是否有进行中的考试记录
            boolean noActiveExams = constraintService.identityDocumentNumberHasNoActiveExams(originalIdentityDocumentNumber);
            BusinessValidationUtil.check(noActiveExams, "该学生有进行中的考试记录，无法修改证件号");
        }

        return super.updateById(entity);
    }

    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Student entity) {
        // 检查证件号是否唯一
        String identityDocumentNumber = entity.getIdentityDocumentNumber();
        if (identityDocumentNumber != null && !identityDocumentNumber.isEmpty()) {
            boolean isUnique = constraintService.isIdentityDocumentNumberUnique(identityDocumentNumber, null);
            BusinessValidationUtil.check(isUnique, "该证件号已被使用");
        }

        return super.save(entity);
    }

    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteStudent(id)) {
            throw new ConstraintViolationException("无法删除该学生，存在关联的考试记录");
        }
        return super.removeById(id);
    }

    @Override
    @Transactional
    public Result registerWithVerificationCode(Student student, String verificationCode) {
        // 检查证件号码是否已存在
        if (lambdaQuery().eq(Student::getIdentityDocumentNumber, student.getIdentityDocumentNumber()).count() > 0) {
            return Result.failed("证件号码已存在");
        }

        // 检查邮箱是否已存在
        if (lambdaQuery().eq(Student::getEmail, student.getEmail()).count() > 0) {
            return Result.failed("邮箱已存在");
        }


//         验证验证码
        if (!emailUtils.verifyCode(student.getEmail(), verificationCode)) {
            return Result.failed("验证码错误或已过期");
        }

        // 加密密码
        student.setPassword(PasswordUtils.encrypt(student.getPassword()));

        // 保存学生信息
        save(student);

        // 清除验证码
        emailUtils.removeVerificationCode(student.getEmail());
        return Result.success();
    }

    @Override
    public Result<TokenVO<StudentVO>> login(String identityDocumentNumber, String password) {
        // 根据证件号查询学生
        Student student = lambdaQuery().eq(Student::getIdentityDocumentNumber, identityDocumentNumber).one();
        if (student == null) {
            return Result.failed("用户不存在");
        }

        // 验证密码
        if (!PasswordUtils.matches(password, student.getPassword())) {
            return Result.failed("密码错误");
        }
        // 生成token
        String token = jwtUtil.generateToken(student.getId(), student.getName());

        // 创建TokenVO
        TokenVO<StudentVO> tokenVO = new TokenVO<>();
        tokenVO.setToken(token);
        tokenVO.setUser(beanConverter.convert(student, StudentVO.class));
        return Result.success(tokenVO);
    }

    @Override
    public void sendResetEmail(String email) {
        // 查询学生
        Student student = lambdaQuery().eq(Student::getEmail, email).one();
        if (student == null) {
            throw new RuntimeException("该邮箱未注册");
        }

        // 生成验证码
        String code = emailUtils.generateVerificationCode();

        // 发送重置密码邮件
        emailUtils.sendResetPasswordEmail(email, code);

        // 保存验证码到缓存
        emailUtils.saveVerificationCode(email, code);
    }

    @Override
    public void resetPassword(String email, String code, String newPassword) {
        // 验证验证码
        if (!emailUtils.verifyCode(email, code)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 查询学生
        Student student = lambdaQuery().eq(Student::getEmail, email).one();
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        // 更新密码
        student.setPassword(PasswordUtils.encrypt(newPassword));
        updateById(student);

        // 清除验证码
        emailUtils.removeVerificationCode(email);
    }
}
