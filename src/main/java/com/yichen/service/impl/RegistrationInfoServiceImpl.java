package com.yichen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.RegistrationInfo;
import com.yichen.entity.Student;
import com.yichen.enums.RegistrationStep;
import com.yichen.mapper.RegistrationInfoMapper;
import com.yichen.service.RegistrationInfoService;
import com.yichen.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationInfoServiceImpl extends ServiceImpl<RegistrationInfoMapper, RegistrationInfo> implements RegistrationInfoService {

    @Autowired
    private StudentService studentService;

    @Override
    @Transactional
    public void completeStep(Long studentId, boolean chooseOral) {
        // 获取学生的报名信息
        RegistrationInfo registrationInfo = getOne(new LambdaQueryWrapper<RegistrationInfo>()
                .eq(RegistrationInfo::getStudentId, studentId));
        
        if (registrationInfo == null) {
            throw new RuntimeException("未找到报名信息");
        }

        // 获取当前步骤
        RegistrationStep current = RegistrationStep.valueOf(registrationInfo.getCurrentStep());
        
        // 获取下一步
        List<RegistrationStep> nextSteps = current.getNextSteps(chooseOral);
        
        if (!nextSteps.isEmpty()) {
            RegistrationStep next = nextSteps.get(0); // 自动选择第一个可用步骤
            registrationInfo.setCurrentStep(next.name());
            updateCompletedSteps(registrationInfo, current);
            updateById(registrationInfo);
        }
    }

    private void updateCompletedSteps(RegistrationInfo registrationInfo, RegistrationStep completed) {
        JSONArray steps = Optional.ofNullable(registrationInfo.getCompletedSteps())
                .map(JSONArray::parseArray)
                .orElse(new JSONArray());
        steps.add(completed.name());
        registrationInfo.setCompletedSteps(steps.toJSONString());
    }
} 