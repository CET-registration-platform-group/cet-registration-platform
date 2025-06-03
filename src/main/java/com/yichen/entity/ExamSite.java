package com.yichen.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("exam_site")
public class ExamSite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;

    // 非持久化字段 - 座位总数
    @TableField(exist = false)
    private Integer totalSeat;

    // 非持久化字段 - 已分配的座位数
    @TableField(exist = false)
    private Integer usedSeat;
}