package com.yichen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.RegistrationInfo;
import com.yichen.vo.RegistrationInfoVO;

public interface RegistrationInfoService extends IService<RegistrationInfo> {
    /**
     * 完成当前步骤并进入下一步
     * @param studentId 学生ID
     * @param registrationInfoVO 报名信息视图对象
     */
    void completeStep(Long studentId, RegistrationInfoVO registrationInfoVO);
} 