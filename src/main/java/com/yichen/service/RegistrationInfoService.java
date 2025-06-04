package com.yichen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.RegistrationInfo;
import com.yichen.vo.RegistrationInfoVO;

public interface RegistrationInfoService extends IService<RegistrationInfo> {
    /**
     * 完成当前步骤并进入下一步
     * @param studentId 学生ID
     * @param completedStep 要完成的步骤名称
     * @throws RuntimeException 当步骤不是合法下一步时抛出异常
     */
    void completeStep(Long studentId, String completedStep);
} 