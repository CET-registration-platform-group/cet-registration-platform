package com.yichen.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ExamSite;
import com.yichen.service.ExamSiteService;
import com.yichen.common.Result;
import com.yichen.vo.ExamSiteVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-sites")
@Api(tags = "考点管理接口", description = "提供考点的增删改查功能")
@RequiredArgsConstructor
public class ExamSiteController {

    private final ExamSiteService examSiteService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取考点列表", notes = "分页获取考点列表，可根据考点名称进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<ExamSiteVO>> list(
        @ApiParam(value = "页码(默认1)", defaultValue = "1", example = "1", required = true)
        @RequestParam(defaultValue = "1") Integer pageNum,
            
        @ApiParam(value = "每页条数(默认10)", defaultValue = "10", example = "10", required = true)
        @RequestParam(defaultValue = "10") Integer pageSize,
            
        @ApiParam(value = "考点名称(支持模糊查询)")
        @RequestParam(required = false) String name
    ) {
        Page<ExamSite> page = examSiteService.getExamSitesWithStatisticsPage(pageNum, pageSize, name);
        Page<ExamSiteVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ExamSiteVO.class));
        return Result.success(result);
    }
    //获取考点列表，不分页
    @ApiOperation(value = "获取考点列表", notes = "获取考点列表，不分页")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/list")
    public Result<List<ExamSiteVO>> list() {
        List<ExamSiteVO> examSiteList = beanConverter.convertList(examSiteService.list(), ExamSiteVO.class);
        return Result.success(examSiteList);
    }


    @ApiOperation(value = "根据ID获取考点", notes = "通过考点ID获取考点详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考点不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<ExamSiteVO> getById(
        @ApiParam(value = "考点ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        ExamSite examSite = examSiteService.getById(id);
        if (examSite == null) {
            return Result.error("考点不存在");
        }
        ExamSiteVO examSiteVO = beanConverter.convert(examSite, ExamSiteVO.class);
        return Result.success(examSiteVO);
    }

    //根据考场获取考点
    @GetMapping("/examSite/{examSiteId}")
    @ApiOperation(value = "根据考场获取考点", notes = "通过考场ID获取考点详细信息")
    public Result<ExamSiteVO> getByExamSiteId(
        @ApiParam(value = "考场ID(唯一标识)", required = true, example = "1")
        @PathVariable Long examSiteId
    ) {
        ExamSite examSite = examSiteService.getByExamSiteId(examSiteId);
        if (examSite == null) {
            return Result.error("考点不存在");
        }
        ExamSiteVO examSiteVO = beanConverter.convert(examSite, ExamSiteVO.class);
        return Result.success(examSiteVO);
    }


    @ApiOperation(value = "添加考点", notes = "创建新的考点")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<ExamSiteVO> add(
        @ApiParam(value = "考点信息(包含考点基本信息和统计信息)", required = true)
        @RequestBody ExamSiteVO examSiteVO
    ) {
        ExamSite examSite = beanConverter.convert(examSiteVO, ExamSite.class);
        boolean success = examSiteService.save(examSite);
        if (success) {
            ExamSiteVO resultVO = beanConverter.convert(examSite, ExamSiteVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加考点失败");
    }

    @ApiOperation(value = "更新考点", notes = "修改现有考点信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "考点不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(
        @ApiParam(value = "考点信息(包含考点ID和需要更新的信息)", required = true)
        @RequestBody ExamSiteVO examSiteVO
    ) {
        if (examSiteVO.getId() == null) {
            return Result.error("考点ID不能为空");
        }
        ExamSite examSite = beanConverter.convert(examSiteVO, ExamSite.class);
        boolean success = examSiteService.updateById(examSite);
        return success ? Result.success() : Result.error("更新考点失败");
    }

    @ApiOperation(value = "删除考点", notes = "通过考点ID删除考点")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考点不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(
        @ApiParam(value = "考点ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        boolean success = examSiteService.removeById(id);
        return success ? Result.success() : Result.error("删除考点失败");
    }
} 