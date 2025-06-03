package com.yichen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("exam_info")
public class ExamInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long examSeatId;
    private LocalDateTime examTime;
    private String examType;
    private String examLevel;
}
