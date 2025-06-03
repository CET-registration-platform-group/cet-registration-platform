package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "学生视图对象", description = "用于前后端交互的学生数据")
public class StudentVO {
    @ApiModelProperty(value = "学生ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "证件类型", example = "0", notes = "0-身份证，1-护照", allowableValues = "0, 1", position = 2)
    private Integer identityDocumentType;
    
    @ApiModelProperty(value = "证件号码", example = "110101199001011234", required = true, position = 3)
    private String identityDocumentNumber;
    
    @ApiModelProperty(value = "姓名", example = "张三", required = true, position = 4)
    private String name;
    
    @ApiModelProperty(value = "邮箱", example = "zhangsan@example.com", position = 5)
    private String email;
    
    @ApiModelProperty(value = "手机号", example = "13800138000", position = 6)
    private String phone;
    
    @ApiModelProperty(value = "密码", example = "password123", position = 7)
    private String password;
}
