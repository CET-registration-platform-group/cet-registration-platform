package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ExamSiteMapper;
import com.yichen.entity.ExamSite;
import com.yichen.service.ConstraintService;
import com.yichen.service.ExamSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考点服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamSiteServiceImpl extends ServiceImpl<ExamSiteMapper, ExamSite> implements ExamSiteService {

    private final ConstraintService constraintService;
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteExamSite(id)) {
            throw new ConstraintViolationException("无法删除该考点，存在关联的考场");
        }
        return super.removeById(id);
    }

    @Override
    public Page<ExamSite> getExamSitesWithStatisticsPage(Integer current, Integer size, String name) {
        Integer offset = (current - 1) * size;
        List<ExamSite> examSites = getBaseMapper().selectWithStatisticsPage(offset, size, name);
        return new Page<ExamSite>(current, size).setRecords(examSites);
    }

    @Override
    public ExamSite getByIdWithStatistics(Long id) {
        return getBaseMapper().getByIdWithStatistics(id);
    }

    @Override
    public ExamSite getByExamSiteId(Long examSiteId) {
        return getBaseMapper().getByExamSiteId(examSiteId);
    }
} 