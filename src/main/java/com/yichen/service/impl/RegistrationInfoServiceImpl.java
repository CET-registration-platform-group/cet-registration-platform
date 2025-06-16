package com.yichen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ExamInfo;
import com.yichen.entity.ExamSeat;
import com.yichen.entity.RegistrationInfo;
import com.yichen.entity.Student;
import com.yichen.enums.RegistrationStep;
import com.yichen.mapper.RegistrationInfoMapper;
import com.yichen.service.ExamInfoService;
import com.yichen.service.ExamSeatService;
import com.yichen.service.RegistrationInfoService;
import com.yichen.service.StudentService;
import com.yichen.utils.BeanConverter;
import com.yichen.vo.RegistrationInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationInfoServiceImpl extends ServiceImpl<RegistrationInfoMapper, RegistrationInfo> implements RegistrationInfoService {

    private final StudentService studentService;
    private final ExamSeatService examSeatService;
    private final ExamInfoService examInfoService;

    @Override
    @Transactional
    public void completeStep(Long studentId, String completedStep) {
        // 获取学生的报名信息
        LambdaQueryWrapper<RegistrationInfo> wrapper = new LambdaQueryWrapper<RegistrationInfo>()
            .eq(RegistrationInfo::getStudentId, studentId);
        RegistrationInfo registrationInfo = getOne(wrapper);

        if (registrationInfo == null) {
            throw new RuntimeException("报名信息不存在");
        }

        // 解析已完成的步骤JSON数组
        JSONArray steps = Optional.ofNullable(registrationInfo.getCompletedSteps())
                .map(JSONArray::parseArray)
                .orElse(new JSONArray());

        if (steps.isEmpty()) {
            if ("AGREEMENT".equals(completedStep)) {
                // 允许完成第一个步骤
                steps.add(completedStep);
                registrationInfo.setCompletedSteps(steps.toJSONString());
                registrationInfo.setCurrentStep(completedStep);
                updateById(registrationInfo);
                return;
            } else {
                throw new RuntimeException("新用户只能从第一步（AGREEMENT）开始");
            }
        }
        // 获取所有已完成步骤的下一步
        List<RegistrationStep> nextSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            String stepName = steps.getString(i);
            RegistrationStep step = RegistrationStep.valueOf(stepName);
            nextSteps.addAll(step.getNextSteps(true));
        }

        // 判断新完成的步骤是否在已完成步骤的下一步中
        RegistrationStep completedStepEnum = RegistrationStep.valueOf(completedStep);
        if (!nextSteps.contains(completedStepEnum)) {
            throw new RuntimeException("当前步骤不是任何已完成步骤的下一步");
        }

        // 如果新完成的步骤不存在于已完成步骤中，则添加
        if (!steps.contains(completedStep)) {
            steps.add(completedStep);
        }

        // 更新completedSteps字段
        registrationInfo.setCompletedSteps(steps.toJSONString());
        registrationInfo.setCurrentStep(completedStep);

        updateById(registrationInfo);

        // 如果是完成最后一步（COMPLETE），则分配座位并生成考试信息
        if (RegistrationStep.COMPLETE.name().equals(completedStep)) {
            // 获取学生信息
            Student student = studentService.getById(studentId);
            if (student == null) {
                throw new RuntimeException("学生信息不存在");
            }

            // 查找空闲座位
            LambdaQueryWrapper<ExamSeat> seatWrapper = new LambdaQueryWrapper<>();
            seatWrapper.eq(ExamSeat::getStatus, 0); // 0表示未占用
            List<ExamSeat> list = examSeatService.list(seatWrapper);
            ExamSeat availableSeat=list.get(0);

            if (availableSeat == null) {
                throw new RuntimeException("没有可用的考试座位");
            }

            // 创建考试信息
            ExamInfo examInfo = ExamInfo.builder()
                .studentId(studentId)
                .examSeatId(availableSeat.getId())
                .examTime(LocalDateTime.now().plusDays(7)) // 假设考试时间在7天后
                .examType("笔试") // 根据实际情况设置
                .examLevel("四级") // 根据实际情况设置
                .build();

            // 保存考试信息（会自动更新座位状态）
            examInfoService.save(examInfo);
        }
    }
}
