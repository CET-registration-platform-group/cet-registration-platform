package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ExamRoom;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ExamSeatMapper;
import com.yichen.entity.ExamSeat;
import com.yichen.service.ConstraintService;
import com.yichen.service.ExamSeatService;
import com.yichen.service.ExamRoomService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试座位服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamSeatServiceImpl extends ServiceImpl<ExamSeatMapper, ExamSeat> implements ExamSeatService {

    private final ConstraintService constraintService;
    private final ExamRoomService examRoomService;

    @Override
    public Page<ExamSeat> listExamSeats(Integer current, Integer size, String seatNumber, Integer status, Long examRoomId) {
        Page<ExamSeat> page = new Page<>(current, size);
        LambdaQueryWrapper<ExamSeat> queryWrapper = new LambdaQueryWrapper<>();
        
        if (seatNumber != null && !seatNumber.isEmpty()) {
            queryWrapper.like(ExamSeat::getSeatNumber, seatNumber);
        }
        
        if (examRoomId != null) {
            queryWrapper.eq(ExamSeat::getExamRoomId, examRoomId);
        }
        
        if (status != null) {
            queryWrapper.eq(ExamSeat::getStatus, status);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ExamSeat entity) {
        // 检查考场是否存在
        Long examRoomId = entity.getExamRoomId();
        if (examRoomId != null) {
            ExamRoom examRoom = examRoomService.getById(examRoomId);
            BusinessValidationUtil.checkNotNull(examRoom, "关联的考场不存在");
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ExamSeat entity) {
        // 检查考场是否存在
        Long examRoomId = entity.getExamRoomId();
        if (examRoomId != null) {
            ExamRoom examRoom = examRoomService.getById(examRoomId);
            BusinessValidationUtil.checkNotNull(examRoom, "关联的考场不存在");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteExamSeat(id)) {
            throw new ConstraintViolationException("无法删除该座位，存在关联的考试记录");
        }
        return super.removeById(id);
    }
} 