package com.yichen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.Student;
import com.yichen.service.StudentService;
import com.yichen.common.Result;
import com.yichen.utils.PasswordUtils;
import com.yichen.vo.StudentVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        String encodedPassword = PasswordUtils.encrypt(student.getPassword());
        student.setPassword(encodedPassword);
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
        String encodedPassword = PasswordUtils.encrypt(student.getPassword());
        student.setPassword(encodedPassword);
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

} 