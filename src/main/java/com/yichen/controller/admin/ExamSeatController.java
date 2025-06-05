package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ExamSeat;
import com.yichen.service.ExamSeatService;
import com.yichen.common.Result;
import com.yichen.vo.ExamSeatVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-seats")
@Api(tags = "考试座位管理接口", description = "提供考试座位的增删改查功能")
@RequiredArgsConstructor
public class ExamSeatController {

    private final ExamSeatService examSeatService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取座位列表", notes = "分页获取座位列表，可根据考场ID和座位号筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
    public Result<Page<ExamSeatVO>> list(
        @ApiParam(value = "页码(默认1)", defaultValue = "1", example = "1", required = true)
        @RequestParam(defaultValue = "1") Integer pageNum,
            
        @ApiParam(value = "每页条数(默认10)", defaultValue = "10", example = "10", required = true)
        @RequestParam(defaultValue = "10") Integer pageSize,
            
        @ApiParam(value = "考场ID(所属考场)")
        @RequestParam(required = false) Long examRoomId,
            
        @ApiParam(value = "座位号(支持模糊查询)")
        @RequestParam(required = false) String seatNumber,

        @ApiParam(value = "座位状态(0-未占用，1-占用)")
        @RequestParam(required = false) Integer status
    ) {
        Integer offset=pageSize*(pageNum-1);
        List<ExamSeatVO> list= examSeatService.getPage(offset,pageSize,examRoomId,seatNumber,status);
        int total =examSeatService.count(examRoomId,  seatNumber, status);
        Page<ExamSeatVO> result = new Page<>(pageNum, pageSize,total);
        result.setRecords(list);
        return Result.success(result);
    }
    //获取座位信息，不分页
    @ApiOperation(value = "获取座位列表", notes = "获取座位列表，不分页，可根据考场ID筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/list")
    public Result<List<ExamSeatVO>> list(
            @ApiParam(value = "考场ID(所属考场)")
            @RequestParam(required = false) Long examRoomId
    ){
        LambdaQueryWrapper<ExamSeat> wrapper = new LambdaQueryWrapper<>();
        List<ExamSeat> examSeats= examSeatService.list(wrapper);
        List<ExamSeatVO> result = beanConverter.convertList(examSeats, ExamSeatVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "根据ID获取座位", notes = "通过座位ID获取座位详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "座位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<ExamSeatVO> getById(
        @ApiParam(value = "座位ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        ExamSeatVO examSeatVO = examSeatService.getExamSeatVOById(id);
        if (examSeatVO == null) {
            return Result.error("座位不存在");
        }
        return Result.success(examSeatVO);
    }

    @ApiOperation(value = "添加座位", notes = "创建新的考试座位")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<ExamSeatVO> add(
        @ApiParam(value = "座位信息(包含座位基本信息和所属考场信息)", required = true)
        @RequestBody ExamSeatVO examSeatVO
    ) {
        ExamSeat examSeat = beanConverter.convert(examSeatVO, ExamSeat.class);
        boolean success = examSeatService.save(examSeat);
        if (success) {
            ExamSeatVO resultVO = beanConverter.convert(examSeat, ExamSeatVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加座位失败");
    }
    //批量添加座位
    @ApiOperation(value = "批量添加座位", notes = "创建新的考试座位")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "座位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/batch")
    public Result addBatch(
        @ApiParam(value = "座位信息(包含座位基本信息和所属考场信息)", required = true)
        @RequestBody List<ExamSeatVO> examSeatVOList
    ) {
        examSeatService.saveBatch(beanConverter.convertList(examSeatVOList, ExamSeat.class));
        return Result.success("添加成功");
    }

    @ApiOperation(value = "更新座位", notes = "修改现有座位信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "座位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(
        @ApiParam(value = "座位信息(包含座位ID和需要更新的信息)", required = true)
        @RequestBody ExamSeatVO examSeatVO
    ) {
        if (examSeatVO.getId() == null) {
            return Result.error("座位ID不能为空");
        }
        ExamSeat examSeat = beanConverter.convert(examSeatVO, ExamSeat.class);
        boolean success = examSeatService.updateById(examSeat);
        return success ? Result.success() : Result.error("更新座位失败");
    }

    @ApiOperation(value = "删除座位", notes = "通过座位ID删除座位")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "座位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(
        @ApiParam(value = "座位ID(唯一标识)", required = true, example = "1")
        @PathVariable Long id
    ) {
        boolean success = examSeatService.removeById(id);
        return success ? Result.success() : Result.error("删除座位失败");
    }
} 