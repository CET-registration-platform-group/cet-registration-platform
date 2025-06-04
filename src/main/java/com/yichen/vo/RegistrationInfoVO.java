package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报名信息视图对象
 */
@Data
@ApiModel(value = "报名信息视图对象", description = "用于前后端交互的报名信息数据")
public class RegistrationInfoVO {
    @ApiModelProperty(value = "报名信息ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "学生ID", example = "1", position = 2)
    private Long studentId;
    
    @ApiModelProperty(value = "当前步骤", example = "AGREEMENT", position = 3)
    private String currentStep;
    
    @ApiModelProperty(value = "已完成步骤", example = "[\"AGREEMENT\",\"QUAL_QUERY\"]", position = 4)
    private String completedSteps;
} 