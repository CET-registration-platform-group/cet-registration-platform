package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "考点视图对象", description = "用于前后端交互的考点数据")
public class ExamSiteVO {
    @ApiModelProperty(value = "考点ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "考点名称", example = "北京大学考点", required = true, position = 2)
    private String name;
    
    @ApiModelProperty(value = "考点地址", example = "北京市海淀区颐和园路5号", required = true, position = 3)
    private String address;

    @ApiModelProperty(value = "座位总数", example = "1000", position = 4)
    private Integer totalSeat;

    @ApiModelProperty(value = "已用座位数", example = "500", position = 5)
    private Integer usedSeat;
}
