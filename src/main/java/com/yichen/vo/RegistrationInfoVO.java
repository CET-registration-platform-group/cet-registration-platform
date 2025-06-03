package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "报名信息")
public class RegistrationInfoVO {
    
    @ApiModelProperty(value = "报名ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "学生ID", example = "1")
    private Long studentId;
    
    @ApiModelProperty(value = "当前步骤", example = "BASIC_INFO", notes = "当前正在进行的报名步骤")
    private String currentStep;
    
    @ApiModelProperty(value = "已完成步骤", example = "BASIC_INFO,EXAM_TYPE", notes = "已完成的报名步骤，多个步骤用逗号分隔")
    private String completedSteps;
    
    @ApiModelProperty(value = "考试ID", example = "1")
    private Long examId;
    
    @ApiModelProperty(value = "考试名称", example = "2024年6月CET-4")
    private String examName;
    
    @ApiModelProperty(value = "考试类型", example = "CET-4")
    private String examType;
    
    @ApiModelProperty(value = "考试时间", example = "2024-06-15 09:00:00")
    private LocalDateTime examTime;
    
    @ApiModelProperty(value = "考试地点", example = "第一教学楼")
    private String examLocation;
    
    @ApiModelProperty(value = "考场号", example = "101")
    private String examRoom;
    
    @ApiModelProperty(value = "座位号", example = "A01")
    private String seatNumber;
    
    @ApiModelProperty(value = "是否选择口试", example = "true")
    private Boolean chooseOral;
    
    @ApiModelProperty(value = "口试时间", example = "2024-06-16 14:00:00")
    private LocalDateTime oralTime;
    
    @ApiModelProperty(value = "口试地点", example = "第二教学楼")
    private String oralLocation;
    
    @ApiModelProperty(value = "口试考场", example = "201")
    private String oralRoom;
    
    @ApiModelProperty(value = "报名状态", example = "1", notes = "0:未报名 1:已报名 2:已缴费 3:已确认 4:已取消")
    private Integer status;
    
    @ApiModelProperty(value = "报名时间", example = "2024-03-01 10:00:00")
    private LocalDateTime registrationTime;
    
    @ApiModelProperty(value = "缴费时间", example = "2024-03-01 10:30:00")
    private LocalDateTime paymentTime;
    
    @ApiModelProperty(value = "确认时间", example = "2024-03-02 09:00:00")
    private LocalDateTime confirmTime;
    
    @ApiModelProperty(value = "取消时间", example = "2024-03-03 14:00:00")
    private LocalDateTime cancelTime;
    
    @ApiModelProperty(value = "备注", example = "特殊情况说明")
    private String remark;
} 