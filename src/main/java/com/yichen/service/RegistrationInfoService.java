package com.yichen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.RegistrationInfo;

public interface RegistrationInfoService extends IService<RegistrationInfo> {
    /**
     * 完成当前步骤并进入下一步
     * @param studentId 学生ID
     * @param chooseOral 是否选择口试
     */
    void completeStep(Long studentId, boolean chooseOral);
} 