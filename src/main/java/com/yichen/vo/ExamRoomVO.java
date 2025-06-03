package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考场视图对象", description = "用于前后端交互的考场数据")
public class ExamRoomVO {
    @ApiModelProperty(value = "考场ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "所属考点ID", example = "1", required = true, position = 2)
    private Long examSiteId;
    
    @ApiModelProperty(value = "考场号", example = "A101", required = true, position = 3)
    private String roomNumber;
}
