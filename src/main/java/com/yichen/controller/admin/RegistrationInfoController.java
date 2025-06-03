package com.yichen.controller.admin;

import com.yichen.entity.RegistrationInfo;
import com.yichen.service.RegistrationInfoService;
import com.yichen.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/registration-info")
public class RegistrationInfoController {

    @Autowired
    private RegistrationInfoService registrationInfoService;

    @GetMapping("/{studentId}")
    public Result<RegistrationInfo> getRegistrationInfo(@PathVariable Long studentId) {
        RegistrationInfo registrationInfo = registrationInfoService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RegistrationInfo>()
                        .eq(RegistrationInfo::getStudentId, studentId)
        );
        return Result.success(registrationInfo);
    }

    @PostMapping("/complete-step")
    public Result completeStep(
            @RequestParam Long studentId,
            @RequestParam(required = false, defaultValue = "false") boolean chooseOral) {
        registrationInfoService.completeStep(studentId, chooseOral);
        return Result.success("步骤完成");
    }
} 