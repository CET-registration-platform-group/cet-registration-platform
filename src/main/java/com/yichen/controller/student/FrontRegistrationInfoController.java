package com.yichen.controller.student;

import com.yichen.common.Result;
import com.yichen.entity.RegistrationInfo;
import com.yichen.service.RegistrationInfoService;
import com.yichen.vo.RegistrationInfoVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/registration")
@Api(tags = "前台报名信息接口", description = "提供学生报名相关的功能")
@RequiredArgsConstructor
public class FrontRegistrationInfoController {

    private final RegistrationInfoService registrationInfoService;
    private final BeanConverter beanConverter;

    @ApiOperation(value = "获取报名信息", notes = "获取学生的报名信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{studentId}")
    public Result<RegistrationInfoVO> getRegistrationInfo(
        @ApiParam(value = "学生ID", required = true, example = "1")
        @PathVariable Long studentId
    ) {
        RegistrationInfo registrationInfo = registrationInfoService.getOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RegistrationInfo>()
                .eq(RegistrationInfo::getStudentId, studentId)
        );
        if (registrationInfo == null) {
            return Result.error("报名信息不存在");
        }
        RegistrationInfoVO vo = beanConverter.convert(registrationInfo, RegistrationInfoVO.class);
        return Result.success(vo);
    }

    @ApiOperation(value = "完成当前步骤", notes = "完成报名流程的当前步骤，并更新当前步骤为下一步")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误或步骤不是合法下一步"),
        @ApiResponse(code = 404, message = "报名信息不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/complete-step")
    public Result completeStep(
        @ApiParam(value = "学生ID", required = true, example = "1")
        @RequestBody Long studentId,
            
        @ApiParam(value = "要完成的步骤名称", required = true, example = "AGREEMENT", 
                 allowableValues = "AGREEMENT,QUAL_QUERY,QUAL_CONFIRM,WRITTEN_APPLY,WRITTEN_PAY,ORAL_APPLY,ORAL_PAY,COMPLETE,PRINT_ADMIT")
        @RequestBody String completedStep
    ) {
        registrationInfoService.completeStep(studentId, completedStep);
        return Result.success("步骤完成");
    }
} 