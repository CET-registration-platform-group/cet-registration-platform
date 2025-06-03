package com.yichen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer identityDocumentType; // 证件类型
    private String identityDocumentNumber; // 证件号码
    private String name; // 姓名
    private String email; // 邮箱
    private String phone; // 手机号
    private String password; // 密码
}
