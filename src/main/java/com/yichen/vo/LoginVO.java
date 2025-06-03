package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录视图对象", description = "用于前后端交互的登录数据")
public class LoginVO {
    @ApiModelProperty(value = "邮箱或证件号", example = "admin@163.com", required = true, position = 1)
    private String username;
    
    @ApiModelProperty(value = "密码", example = "admin123", required = true, position = 2)
    private String password;
} 