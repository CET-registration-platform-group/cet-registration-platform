package com.yichen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.RegistrationInfo;
import com.yichen.entity.Student;
import com.yichen.enums.RegistrationStep;
import com.yichen.mapper.RegistrationInfoMapper;
import com.yichen.service.RegistrationInfoService;
import com.yichen.service.StudentService;
import com.yichen.vo.RegistrationInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationInfoServiceImpl extends ServiceImpl<RegistrationInfoMapper, RegistrationInfo> implements RegistrationInfoService {

    @Autowired
    private StudentService studentService;

    @Override
    @Transactional
    public void completeStep(Long studentId, RegistrationInfoVO registrationInfoVO) {
        // 获取学生的报名信息
        RegistrationInfo registrationInfo = getOne(new LambdaQueryWrapper<RegistrationInfo>()
                .eq(RegistrationInfo::getStudentId, studentId));
        
        if (registrationInfo == null) {
            throw new RuntimeException("未找到报名信息");
        }

        // 获取当前步骤
        RegistrationStep current = RegistrationStep.valueOf(registrationInfo.getCurrentStep());

        // 解析已完成的步骤JSON数组
        JSONArray steps = Optional.ofNullable(registrationInfo.getCompletedSteps())
                .map(JSONArray::parseArray)
                .orElse(new JSONArray());
        
        // 获取所有已完成步骤的下一步
        List<RegistrationStep> nextSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            String stepName = steps.getString(i);
            RegistrationStep completedStep = RegistrationStep.valueOf(stepName);
            nextSteps.addAll(completedStep.getNextSteps(true));
        }
        
        // 判断新完成的步骤是否在已完成步骤的下一步中
        if (!nextSteps.contains(current)) {
            throw new RuntimeException("当前步骤不是任何已完成步骤的下一步");
        }

        // 如果新完成的步骤不存在于已完成步骤中，则添加
        if (!steps.contains(current.name())) {
            steps.add(current.name());
        }
        // 更新completedSteps字段
        registrationInfo.setCompletedSteps(steps.toJSONString());
        updateById(registrationInfo);
    }
    
} 