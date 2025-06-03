package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.mapper.StudentMapper;
import com.yichen.entity.Student;
import com.yichen.service.AuthService;
import com.yichen.utils.BeanConverter;
import com.yichen.utils.JwtUtil;
import com.yichen.utils.PasswordEncoder;
import com.yichen.vo.TokenVO;
import com.yichen.vo.StudentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final BeanConverter beanConverter;
    private final JwtUtil jwtUtil;

    @Override
    public TokenVO login(String username, String password) {
        // 使用邮箱或证件号作为登录名查询
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getEmail, username)
                .or()
                .eq(Student::getIdentityDocumentNumber, username);
        
        Student student = studentMapper.selectOne(queryWrapper);
        
        // 判断学生是否存在以及密码是否正确
        if (student != null && passwordEncoder.matches(password, student.getPassword())) {
            // 生成token
            String token = jwtUtil.generateToken(student.getId(), student.getName());
            
            // 创建并返回TokenVO
            TokenVO tokenVO = new TokenVO();
            tokenVO.setToken(token);
            tokenVO.setUser(beanConverter.convert(student, StudentVO.class));
            
            return tokenVO;
        }
        
        return null;
    }

    @Override
    public boolean logout() {
        // 无状态认证不需要服务端处理登出
        return true;
    }
} 