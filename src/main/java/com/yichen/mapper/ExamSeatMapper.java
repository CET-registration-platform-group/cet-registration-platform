package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ExamSeat;
import com.yichen.vo.ExamSeatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSeatMapper extends BaseMapper<ExamSeat> {
    List<ExamSeatVO> getPage(Integer offset, Integer pageSize, Long examRoomId, String seatNumber, Integer status);

    int countPage(Long examRoomId, String seatNumber, Integer status);
}
