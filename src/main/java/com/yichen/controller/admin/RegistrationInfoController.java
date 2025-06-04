package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.RegistrationInfo;
import com.yichen.entity.Student;
import com.yichen.service.RegistrationInfoService;
import com.yichen.service.StudentService;
import com.yichen.common.Result;
import com.yichen.utils.BeanConverter;
import com.yichen.vo.RegistrationInfoVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "报名信息管理", description = "提供报名信息相关的增删改查功能")
@RequestMapping("/api/admin/registration-infos")
@RequiredArgsConstructor
public class RegistrationInfoController {

    private final RegistrationInfoService registrationInfoService;
    private final StudentService studentService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "分页查询报名信息", notes = "分页获取报名信息列表，支持按学生姓名、证件号等进行筛选")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "当前页码(从1开始)", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "studentName", value = "学生姓名", paramType = "query", dataType = "string"),
        @ApiImplicitParam(name = "identityDocumentNumber", value = "证件号码", paramType = "query", dataType = "string")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<RegistrationInfoVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String identityDocumentNumber) {
        
        Page<RegistrationInfo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RegistrationInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        // 如果提供了学生姓名或证件号，先查询符合条件的学生
        if (studentName != null || identityDocumentNumber != null) {
            LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
            if (studentName != null && !studentName.isEmpty()) {
                studentWrapper.like(Student::getName, studentName);
            }
            if (identityDocumentNumber != null && !identityDocumentNumber.isEmpty()) {
                studentWrapper.like(Student::getIdentityDocumentNumber, identityDocumentNumber);
            }
            
            List<Student> students = studentService.list(studentWrapper);
            if (students.isEmpty()) {
                // 如果没有找到符合条件的学生，直接返回空结果
                Page<RegistrationInfoVO> emptyPage = new Page<>(pageNum, pageSize);
                return Result.success(emptyPage);
            }
            
            // 根据找到的学生ID筛选报名信息
            queryWrapper.in(RegistrationInfo::getStudentId, 
                    students.stream().map(Student::getId).toArray());
        }
        
        // 执行分页查询
        Page<RegistrationInfo> resultPage = registrationInfoService.page(page, queryWrapper);
        
        // 转换为VO对象
        Page<RegistrationInfoVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<RegistrationInfoVO> voList = beanConverter.convertList(resultPage.getRecords(), RegistrationInfoVO.class);
        
        // 填充学生信息
        for (RegistrationInfoVO vo : voList) {
            Student student = studentService.getById(vo.getStudentId());
            if (student != null) {
                // 不再需要填充额外字段，因为都存储在registrationInfo JSON中
            }
        }
        
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    @ApiOperation(value = "获取报名信息详情", notes = "根据ID获取报名信息详情")
    @ApiImplicitParam(name = "id", value = "报名信息ID", required = true, paramType = "path", dataType = "long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<RegistrationInfoVO> getById(@PathVariable Long id) {
        RegistrationInfo registrationInfo = registrationInfoService.getById(id);
        if (registrationInfo == null) {
            return Result.error(404, "报名信息不存在");
        }
        
        RegistrationInfoVO vo = beanConverter.convert(registrationInfo, RegistrationInfoVO.class);
        
        // 填充学生信息
        Student student = studentService.getById(registrationInfo.getStudentId());
        if (student != null) {
            // 不再需要填充额外字段，因为都存储在registrationInfo JSON中
        }
        
        return Result.success(vo);
    }

    @ApiOperation(value = "添加报名信息", notes = "添加新的报名信息")
    @ApiParam(value = "报名信息对象", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "添加成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<RegistrationInfoVO> add(@RequestBody RegistrationInfoVO registrationInfoVO) {
        // 验证学生是否存在
        Student student = studentService.getById(registrationInfoVO.getStudentId());
        if (student == null) {
            return Result.error(400, "学生不存在");
        }
        
        // 验证是否已存在报名信息
        LambdaQueryWrapper<RegistrationInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationInfo::getStudentId, registrationInfoVO.getStudentId());
        if (registrationInfoService.count(queryWrapper) > 0) {
            return Result.error(400, "该学生已有报名信息");
        }
        
        // 转换为实体对象
        RegistrationInfo registrationInfo = beanConverter.convert(registrationInfoVO, RegistrationInfo.class);
        
        // 保存报名信息
        boolean success = registrationInfoService.save(registrationInfo);
        if (!success) {
            return Result.error(500, "添加报名信息失败");
        }
        
        // 返回保存后的对象
        RegistrationInfoVO resultVO = beanConverter.convert(registrationInfo, RegistrationInfoVO.class);
        return Result.success(resultVO);
    }

    @ApiOperation(value = "更新报名信息", notes = "更新已有的报名信息")
    @ApiParam(value = "报名信息对象", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "更新成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(@RequestBody RegistrationInfoVO registrationInfoVO) {
        // 验证ID是否存在
        if (registrationInfoVO.getId() == null) {
            return Result.error(400, "ID不能为空");
        }
        
        // 验证报名信息是否存在
        if (!registrationInfoService.getById(registrationInfoVO.getId()).getStudentId().equals(registrationInfoVO.getStudentId())) {
            return Result.error(400, "不能修改学生ID");
        }
        
        // 转换为实体对象
        RegistrationInfo registrationInfo = beanConverter.convert(registrationInfoVO, RegistrationInfo.class);
        
        // 更新报名信息
        boolean success = registrationInfoService.updateById(registrationInfo);
        if (!success) {
            return Result.error(500, "更新报名信息失败");
        }
        
        return Result.success();
    }

    @ApiOperation(value = "删除报名信息", notes = "根据ID删除报名信息")
    @ApiImplicitParam(name = "id", value = "报名信息ID", required = true, paramType = "path", dataType = "long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "删除成功"),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        // 验证报名信息是否存在
        if (registrationInfoService.getById(id) == null) {
            return Result.error(404, "报名信息不存在");
        }
        
        // 删除报名信息
        boolean success = registrationInfoService.removeById(id);
        if (!success) {
            return Result.error(500, "删除报名信息失败");
        }
        
        return Result.success();
    }
    
    @ApiOperation(value = "重置报名步骤", notes = "将学生的报名步骤重置到初始状态")
    @ApiImplicitParam(name = "studentId", value = "学生ID", required = true, paramType = "path", dataType = "long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "重置成功"),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/reset/{studentId}")
    public Result<Void> resetRegistrationStep(@PathVariable Long studentId) {
        // 查找学生的报名信息
        LambdaQueryWrapper<RegistrationInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationInfo::getStudentId, studentId);
        RegistrationInfo registrationInfo = registrationInfoService.getOne(queryWrapper);
        
        if (registrationInfo == null) {
            return Result.error(404, "报名信息不存在");
        }
        
        // 重置为初始步骤
        registrationInfo.setCurrentStep("BASIC_INFO");
        registrationInfo.setCompletedSteps("[]"); // 清空已完成步骤
        
        // 更新报名信息
        boolean success = registrationInfoService.updateById(registrationInfo);
        if (!success) {
            return Result.error(500, "重置报名步骤失败");
        }
        
        return Result.success();
    }
} 