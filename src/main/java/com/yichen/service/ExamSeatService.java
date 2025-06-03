package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ExamSeat;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试座位服务接口
 */
public interface ExamSeatService extends IService<ExamSeat> {
    /**
     * 分页查询座位列表，支持按座位号、状态和考场ID筛选
     * @param current 当前页
     * @param size 每页数量
     * @param seatNumber 座位号（可选）
     * @param status 状态（可选）：0-未占用，1-占用
     * @param examRoomId 考场ID（可选）
     * @return 座位分页数据
     */
    Page<ExamSeat> listExamSeats(Integer current, Integer size, String seatNumber, Integer status, Long examRoomId);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);
}