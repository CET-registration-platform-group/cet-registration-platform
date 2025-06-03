package com.yichen.controller.student;

import com.yichen.common.Result;
import com.yichen.entity.Student;
import com.yichen.service.StudentService;
import com.yichen.utils.EmailUtils;
import com.yichen.vo.StudentRegisterVO;
import com.yichen.vo.StudentVO;
import com.yichen.utils.BeanConverter;
import com.yichen.utils.JwtUtil;
import com.yichen.vo.TokenVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Api(tags = "前台学生接口", description = "提供学生相关的功能")
@RequiredArgsConstructor
public class FrontStudentController {

    private final StudentService studentService;
    private final BeanConverter beanConverter;
    private final EmailUtils emailUtils;
    private final JwtUtil jwtUtil;

    @ApiOperation(value = "发送邮箱验证码", notes = "发送邮箱验证码用于注册")
    @ApiResponses({
        @ApiResponse(code = 200, message = "发送成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/send-verification-code")
    public Result sendVerificationCode(
        @ApiParam(value = "邮箱地址", required = true, example = "student@example.com")
        @RequestParam String email
    ) {
        // 检查邮箱是否已存在
        if (studentService.lambdaQuery().eq(Student::getEmail, email).count() > 0) {
            return Result.error("邮箱已存在");
        }

        // 生成验证码并发送邮件
        String code = emailUtils.generateVerificationCode();
        emailUtils.sendVerificationEmail(email, code);
        
        return Result.success("验证码已发送，请查收邮件");
    }

    @ApiOperation(value = "学生注册", notes = "学生注册并验证邮箱")
    @ApiResponses({
        @ApiResponse(code = 200, message = "注册成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/register")
    public Result register(
        @ApiParam(value = "注册信息", required = true)
        @Validated @RequestBody StudentRegisterVO registerVO
    ) {
        Student student = beanConverter.convert(registerVO, Student.class);
        studentService.registerWithVerificationCode(student, registerVO.getVerificationCode());
        return Result.success("注册成功");
    }

    @ApiOperation(value = "学生登录", notes = "学生使用证件号码和密码登录")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功", response = Result.class),
        @ApiResponse(code = 400, message = "证件号码或密码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/login")
    public Result<TokenVO<StudentVO>> login(
        @ApiParam(value = "证件号码", required = true, example = "123456789012345678")
        @RequestParam String identityDocumentNumber,
            
        @ApiParam(value = "密码", required = true)
        @RequestParam String password
    ) {
        Student student = studentService.login(identityDocumentNumber, password);
        
        // 生成token
        String token = jwtUtil.generateToken(student.getId(), student.getName());
        
        // 创建TokenVO
        TokenVO<StudentVO> tokenVO = new TokenVO<>();
        tokenVO.setToken(token);
        tokenVO.setUser(beanConverter.convert(student, StudentVO.class));
        
        return Result.success(tokenVO);
    }

    @ApiOperation(value = "发送重置密码邮件", notes = "向指定邮箱发送重置密码验证码")
    @ApiResponses({
        @ApiResponse(code = 200, message = "发送成功", response = Result.class),
        @ApiResponse(code = 400, message = "邮箱不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/send-reset-email")
    public Result sendResetEmail(
        @ApiParam(value = "邮箱地址", required = true, example = "student@example.com")
        @RequestParam String email
    ) {
        studentService.sendResetEmail(email);
        return Result.success("重置密码邮件已发送");
    }

    @ApiOperation(value = "重置密码", notes = "使用验证码重置密码")
    @ApiResponses({
        @ApiResponse(code = 200, message = "重置成功", response = Result.class),
        @ApiResponse(code = 400, message = "验证码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/reset-password")
    public Result resetPassword(
        @ApiParam(value = "邮箱地址", required = true, example = "student@example.com")
            @RequestParam String email,
            
        @ApiParam(value = "验证码", required = true, example = "123456")
            @RequestParam String code,
            
        @ApiParam(value = "新密码", required = true)
        @RequestParam String newPassword
    ) {
        studentService.resetPassword(email, code, newPassword);
        return Result.success("密码重置成功");
    }
} 