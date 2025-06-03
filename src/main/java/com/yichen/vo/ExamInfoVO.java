package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "考试信息视图对象", description = "用于前后端交互的考试信息数据")
public class ExamInfoVO {
    @ApiModelProperty(value = "考试信息ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "学生ID", example = "1", required = true, position = 2)
    private Long studentId;
    
    @ApiModelProperty(value = "座位ID", example = "1", required = true, position = 3)
    private Long examSeatId;
    
    @ApiModelProperty(value = "考试时间", example = "2023-12-15 09:00:00", required = true, position = 4)
    private LocalDateTime examTime;
    
    @ApiModelProperty(value = "考试类型", example = "笔试", notes = "笔试/口试", required = true, position = 5)
    private String examType;
    
    @ApiModelProperty(value = "考试级别", example = "四级", notes = "四级/六级", required = true, position = 6)
    private String examLevel;
}
