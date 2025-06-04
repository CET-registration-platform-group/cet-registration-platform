package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ExamSite;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ExamRoomMapper;
import com.yichen.entity.ExamRoom;
import com.yichen.service.ConstraintService;
import com.yichen.service.ExamSiteService;
import com.yichen.service.ExamRoomService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考场服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamRoomServiceImpl extends ServiceImpl<ExamRoomMapper, ExamRoom> implements ExamRoomService {

    private final ConstraintService constraintService;
    private final ExamSiteService examSiteService;

    @Override
    public Page<ExamRoom> listExamRooms(Integer current, Integer size, String roomNumber, Long examSiteId) {
        Page<ExamRoom> page = new Page<>(current, size);
        LambdaQueryWrapper<ExamRoom> queryWrapper = new LambdaQueryWrapper<>();
        
        if (roomNumber != null && !roomNumber.isEmpty()) {
            queryWrapper.like(ExamRoom::getRoomNumber, roomNumber);
        }
        
        if (examSiteId != null) {
            queryWrapper.eq(ExamRoom::getExamSiteId, examSiteId);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ExamRoom entity) {
        // 检查考点是否存在
        Long examSiteId = entity.getExamSiteId();
        if (examSiteId != null) {
            ExamSite examSite = examSiteService.getById(examSiteId);
            BusinessValidationUtil.checkNotNull(examSite, "关联的考点不存在");
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ExamRoom entity) {
        // 检查考点是否存在
        Long examSiteId = entity.getExamSiteId();
        if (examSiteId != null) {
            ExamSite examSite = examSiteService.getById(examSiteId);
            BusinessValidationUtil.checkNotNull(examSite, "关联的考点不存在");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteExamRoom(id)) {
            throw new ConstraintViolationException("无法删除该考场，存在关联的座位");
        }
        return super.removeById(id);
    }

    @Override
    public List<ExamRoom> getByExamSiteId(Long examSiteId) {
        return getBaseMapper().getByExamSiteId(examSiteId);
    }
} 