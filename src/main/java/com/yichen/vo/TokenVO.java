package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Token视图对象，用于登录成功后返回token和用户信息
 */
@Data
@ApiModel(value = "令牌视图对象", description = "用于前后端交互的登录token数据")
public class TokenVO {
    @ApiModelProperty(value = "认证令牌", example = "eyJhbGciOiJIUzI1NiJ9.XXX", position = 1)
    private String token;
    
    @ApiModelProperty(value = "学生信息", position = 2)
    private StudentVO user;
} 