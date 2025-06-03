package com.yichen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.Student;
import com.yichen.service.StudentService;
import com.yichen.common.Result;
import com.yichen.vo.StudentVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@Api(tags = "学生管理接口", description = "提供学生的增删改查功能")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取学生列表", notes = "分页获取学生列表，可根据姓名、证件号码和邮箱筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<StudentVO>> list(
        @ApiParam(value = "页码(默认1)", defaultValue = "1", example = "1", required = true)
        @RequestParam(defaultValue = "1") Integer pageNum,
            
        @ApiParam(value = "每页条数(默认10)", defaultValue = "10", example = "10", required = true)
        @RequestParam(defaultValue = "10") Integer pageSize,
            
        @ApiParam(value = "姓名(支持模糊查询)")
            @RequestParam(required = false) String name,
            
        @ApiParam(value = "证件号码(支持模糊查询)")
        @RequestParam(required = false) String idNumber,
        
        @ApiParam(value = "邮箱(支持模糊查询)")
        @RequestParam(required = false) String email
    ) {
        Page<Student> page = studentService.page(new Page<>(pageNum, pageSize));
        Page<StudentVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), StudentVO.class));
        return Result.success(result);
    }

    @ApiOperation(value = "根据ID获取学生", notes = "通过学生ID获取学生详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "学生不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<StudentVO> getById(
        @ApiParam(value = "学生ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        Student student = studentService.getById(id);
        if (student == null) {
            return Result.error("学生不存在");
        }
        StudentVO studentVO = beanConverter.convert(student, StudentVO.class);
        return Result.success(studentVO);
    }

    @ApiOperation(value = "添加学生", notes = "创建新的学生信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<StudentVO> add(
        @ApiParam(value = "学生信息(包含学生基本信息和联系方式)", required = true)
        @RequestBody StudentVO studentVO
    ) {
        Student student = beanConverter.convert(studentVO, Student.class);
        boolean success = studentService.save(student);
        if (success) {
            StudentVO resultVO = beanConverter.convert(student, StudentVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加学生失败");
    }

    @ApiOperation(value = "更新学生", notes = "修改现有学生信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "学生不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(
        @ApiParam(value = "学生信息(包含学生ID和需要更新的信息)", required = true)
        @RequestBody StudentVO studentVO
    ) {
        if (studentVO.getId() == null) {
            return Result.error("学生ID不能为空");
        }
        Student student = beanConverter.convert(studentVO, Student.class);
        boolean success = studentService.updateById(student);
        return success ? Result.success() : Result.error("更新学生失败");
    }

    @ApiOperation(value = "删除学生", notes = "通过学生ID删除学生")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "学生不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(
        @ApiParam(value = "学生ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        boolean success = studentService.removeById(id);
        return success ? Result.success() : Result.error("删除学生失败");
    }

    @ApiOperation(value = "学生注册", notes = "学生注册并发送验证邮件")
    @ApiResponses({
        @ApiResponse(code = 200, message = "注册成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/register")
    public Result register(
        @ApiParam(value = "学生信息(包含基本信息和密码)", required = true)
        @RequestBody StudentVO studentVO
    ) {
        Student student = beanConverter.convert(studentVO, Student.class);
        studentService.register(student);
        return Result.success("注册成功，请查收验证邮件");
        }

    @ApiOperation(value = "邮箱验证", notes = "验证学生邮箱")
    @ApiResponses({
        @ApiResponse(code = 200, message = "验证成功", response = Result.class),
        @ApiResponse(code = 400, message = "验证码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/verify-email")
    public Result verifyEmail(
        @ApiParam(value = "邮箱地址", required = true, example = "student@example.com")
        @RequestParam String email,
            
        @ApiParam(value = "验证码", required = true, example = "123456")
        @RequestParam String code
    ) {
        studentService.verifyEmail(email, code);
        return Result.success("邮箱验证成功");
    }

    @ApiOperation(value = "学生登录", notes = "学生使用证件号码和密码登录")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功", response = Result.class),
        @ApiResponse(code = 400, message = "证件号码或密码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/login")
    public Result<StudentVO> login(
        @ApiParam(value = "证件号码", required = true, example = "123456789012345678")
        @RequestParam String identityDocumentNumber,
            
        @ApiParam(value = "密码", required = true)
        @RequestParam String password
    ) {
        Student student = studentService.login(identityDocumentNumber, password);
        StudentVO studentVO = beanConverter.convert(student, StudentVO.class);
        return Result.success(studentVO);
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