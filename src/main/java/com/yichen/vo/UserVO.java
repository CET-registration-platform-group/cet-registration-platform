package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户视图对象", description = "用于前后端交互的用户数据")
public class UserVO {
    @ApiModelProperty(value = "用户ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "用户名", example = "zhangsan", required = true, position = 2)
    private String username;
    
    @ApiModelProperty(value = "邮箱地址", example = "zhangsan@example.com", position = 3)
    private String email;
    
    @ApiModelProperty(value = "电话号码", example = "13800138000", position = 4)
    private String tel;
}
