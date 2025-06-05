package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ExamInfo;
import com.yichen.service.ExamInfoService;
import com.yichen.common.Result;
import com.yichen.vo.ExamInfoVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/exam-infos")
@Api(tags = "考试信息管理接口", description = "提供考试信息的增删改查功能")
@RequiredArgsConstructor
public class ExamInfoController {

    private final ExamInfoService examInfoService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取考试信息列表", notes = "分页获取考试信息列表，可根据考试类型和考试时间筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<ExamInfoVO>> list(
        @ApiParam(value = "页码(默认1)", defaultValue = "1", example = "1", required = true)
        @RequestParam(defaultValue = "1") Integer pageNum,
            
        @ApiParam(value = "每页条数(默认10)", defaultValue = "10", example = "10", required = true)
        @RequestParam(defaultValue = "10") Integer pageSize,
            
        @ApiParam(value = "考试类型(支持模糊查询)")
            @RequestParam(required = false) String examType,
            
        @ApiParam(value = "考试时间(格式：yyyy-MM-dd)")
        @RequestParam(required = false) String examTime
    ) {
        LambdaQueryWrapper<ExamInfo> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(examType)){
            wrapper.like(ExamInfo::getExamType, examType);
        }
        if (StringUtils.isNotBlank(examTime)){
            wrapper.eq(ExamInfo::getExamTime, examTime);
        }
        Page<ExamInfo> page = examInfoService.page(new Page<>(pageNum, pageSize),  wrapper);
        Page<ExamInfoVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ExamInfoVO.class));
        return Result.success(result);
    }

    @ApiOperation(value = "根据ID获取考试信息", notes = "通过考试信息ID获取详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考试信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<ExamInfoVO> getById(
        @ApiParam(value = "考试信息ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        ExamInfo examInfo = examInfoService.getById(id);
        if (examInfo == null) {
            return Result.error("考试信息不存在");
        }
        ExamInfoVO examInfoVO = beanConverter.convert(examInfo, ExamInfoVO.class);
        return Result.success(examInfoVO);
    }

    @ApiOperation(value = "添加考试信息", notes = "创建新的考试信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<ExamInfoVO> add(
        @ApiParam(value = "考试信息(包含考试类型、时间、地点等信息)", required = true)
        @RequestBody ExamInfoVO examInfoVO
    ) {
        ExamInfo examInfo = beanConverter.convert(examInfoVO, ExamInfo.class);
        boolean success = examInfoService.save(examInfo);
        if (success) {
            ExamInfoVO resultVO = beanConverter.convert(examInfo, ExamInfoVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加考试信息失败");
    }

    @ApiOperation(value = "更新考试信息", notes = "修改现有考试信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "考试信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(
        @ApiParam(value = "考试信息(包含考试信息ID和需要更新的信息)", required = true)
        @RequestBody ExamInfoVO examInfoVO
    ) {
        if (examInfoVO.getId() == null) {
            return Result.error("考试信息ID不能为空");
        }
        ExamInfo examInfo = beanConverter.convert(examInfoVO, ExamInfo.class);
        boolean success = examInfoService.updateById(examInfo);
        return success ? Result.success() : Result.error("更新考试信息失败");
    }

    @ApiOperation(value = "删除考试信息", notes = "通过考试信息ID删除")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考试信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(
        @ApiParam(value = "考试信息ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        boolean success = examInfoService.removeById(id);
        return success ? Result.success() : Result.error("删除考试信息失败");
    }
} 