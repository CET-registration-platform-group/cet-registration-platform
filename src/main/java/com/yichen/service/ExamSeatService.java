package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ExamSeat;
import com.yichen.vo.ExamSeatVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考试座位服务接口
 */
public interface ExamSeatService extends IService<ExamSeat> {

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);

    List<ExamSeatVO> getPage(Integer offset, Integer limit, Long examRoomId, String seatNumber, Integer status);

    int count(Long examRoomId, String seatNumber, Integer status);
}