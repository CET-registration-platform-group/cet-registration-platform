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
import com.yichen.vo.ExamSeatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 考试座位服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamSeatServiceImpl extends ServiceImpl<ExamSeatMapper, ExamSeat> implements ExamSeatService {

    private final ConstraintService constraintService;
    private final ExamRoomService examRoomService;

    
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

    public ExamSeatVO getExamSeatVOById(Long id) {
        return baseMapper.getExamSeatVOById(id);
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

    @Override
    public List<ExamSeatVO> getPage(Integer offset, Integer pageSize, Long examRoomId, String seatNumber, Integer status) {
        return getBaseMapper().getPage(offset, pageSize, examRoomId, seatNumber, status);
    }

    @Override
    public int count(Long examRoomId, String seatNumber, Integer status) {
        return getBaseMapper().countPage(examRoomId, seatNumber, status);
    }
} 