package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录结果对象", description = "登录成功后返回的token和学生信息")
public class LoginResultVO {
    @ApiModelProperty(value = "JWT令牌", example = "xxx.yyy.zzz", required = true)
    private String token;

    @ApiModelProperty(value = "学生信息", required = true)
    private StudentVO student;
}
