package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "学生注册信息")
public class StudentRegisterVO {

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @NotBlank(message = "证件号码不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$", message = "证件号码格式不正确")
    @ApiModelProperty(value = "证件号码", required = true, example = "110101200001011234")
    private String identityDocumentNumber;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "密码必须包含大小写字母和数字，且长度不少于8位")
    @ApiModelProperty(value = "密码", required = true, example = "Password123")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱", required = true, example = "student@example.com")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", required = true, example = "13800138000")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "邮箱验证码", required = true, example = "123456")
    private String verificationCode;

    @NotNull(message = "证件类型不能为空")
    @ApiModelProperty(value = "证件类型", required = true, example = "0")
    private Integer identityDocumentType;
}
