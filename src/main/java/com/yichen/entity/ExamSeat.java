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
@TableName("exam_seat")
public class ExamSeat {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long examRoomId; // 考场ID
    private String seatNumber; // 座位号
    private Integer status; // 状态 0-未占用 1-占用
}
