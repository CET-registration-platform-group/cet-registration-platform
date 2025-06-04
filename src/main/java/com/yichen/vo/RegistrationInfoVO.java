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
    
    @ApiModelProperty(value = "考试ID", example = "1", position = 3)
    private Long examId;
    
    @ApiModelProperty(value = "报名信息JSON", example = "{\"name\":\"张三\",\"gender\":\"男\"}", position = 4)
    private String registrationInfo;
    
    @ApiModelProperty(value = "报名状态", example = "PENDING", position = 5)
    private String status;
} 