package com.yichen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("registration_info")
public class RegistrationInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String currentStep;
    private String completedSteps;
} 