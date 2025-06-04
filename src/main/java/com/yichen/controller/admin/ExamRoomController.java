package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ExamRoom;
import com.yichen.service.ExamRoomService;
import com.yichen.common.Result;
import com.yichen.vo.ExamRoomVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-rooms")
@Api(tags = "考场管理接口", description = "提供考场的增删改查功能")
@RequiredArgsConstructor
public class ExamRoomController {

    private final ExamRoomService examRoomService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取考场列表", notes = "分页获取考场列表，可根据考场号和考点ID筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<ExamRoomVO>> list(
        @ApiParam(value = "页码(默认1)", defaultValue = "1", example = "1", required = true)
        @RequestParam(defaultValue = "1") Integer pageNum,
            
        @ApiParam(value = "每页条数(默认10)", defaultValue = "10", example = "10", required = true)
        @RequestParam(defaultValue = "10") Integer pageSize,
            
        @ApiParam(value = "考场号(支持模糊查询)")
            @RequestParam(required = false) String roomNumber,
            
        @ApiParam(value = "考点ID(所属考点)")
        @RequestParam(required = false) Long examSiteId
    ) {
        Page<ExamRoom> page = examRoomService.page(new Page<>(pageNum, pageSize));
        Page<ExamRoomVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ExamRoomVO.class));
        return Result.success(result);
    }

    //获取考场列表，不分页
    @ApiOperation(value = "获取考场列表", notes = "获取考场列表，不分页，可根据考点ID筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/list")
    public Result<List<ExamRoomVO>> listAll(
        @ApiParam(value = "考点ID(所属考点)")
        @RequestParam(required = false) Long examSiteId
    ) {
        LambdaQueryWrapper<ExamRoom> wrapper = new LambdaQueryWrapper<ExamRoom>().eq(ExamRoom::getExamSiteId, examSiteId);
        List<ExamRoomVO>  list= beanConverter.convertList(examRoomService.list(wrapper), ExamRoomVO.class);
        return Result.success(list);
    }


    @ApiOperation(value = "根据ID获取考场", notes = "通过考场ID获取考场详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<ExamRoomVO> getById(
        @ApiParam(value = "考场ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        ExamRoom examRoom = examRoomService.getById(id);
        if (examRoom == null) {
            return Result.error("考场不存在");
        }
        ExamRoomVO examRoomVO = beanConverter.convert(examRoom, ExamRoomVO.class);
        return Result.success(examRoomVO);
    }

    @ApiOperation(value = "添加考场", notes = "创建新的考场")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<ExamRoomVO> add(
        @ApiParam(value = "考场信息(包含考场基本信息和所属考点信息)", required = true)
        @RequestBody ExamRoomVO examRoomVO
    ) {
        ExamRoom examRoom = beanConverter.convert(examRoomVO, ExamRoom.class);
        boolean success = examRoomService.save(examRoom);
        if (success) {
            ExamRoomVO resultVO = beanConverter.convert(examRoom, ExamRoomVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加考场失败");
    }

    @ApiOperation(value = "更新考场", notes = "修改现有考场信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "考场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(
        @ApiParam(value = "考场信息(包含考场ID和需要更新的信息)", required = true)
        @RequestBody ExamRoomVO examRoomVO
    ) {
        if (examRoomVO.getId() == null) {
            return Result.error("考场ID不能为空");
        }
        ExamRoom examRoom = beanConverter.convert(examRoomVO, ExamRoom.class);
        boolean success = examRoomService.updateById(examRoom);
        return success ? Result.success() : Result.error("更新考场失败");
    }

    @ApiOperation(value = "删除考场", notes = "通过考场ID删除考场")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "考场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(
        @ApiParam(value = "考场ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        boolean success = examRoomService.removeById(id);
        return success ? Result.success() : Result.error("删除考场失败");
    }
} 