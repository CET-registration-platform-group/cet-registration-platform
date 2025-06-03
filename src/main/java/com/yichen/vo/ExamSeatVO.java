package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考试座位视图对象", description = "用于前后端交互的考试座位数据")
public class ExamSeatVO {
    @ApiModelProperty(value = "考试座位ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "所属考场ID", example = "1", required = true, position = 2)
    private Long examRoomId;
    
    @ApiModelProperty(value = "考试座位状态", example = "0", notes = "0-未占用，1-占用", allowableValues = "0, 1", position = 3)
    private Integer status;

    @ApiModelProperty(value = "座位号", example = "A-25", required = true, position = 4)
    private String seatNumber;
}
