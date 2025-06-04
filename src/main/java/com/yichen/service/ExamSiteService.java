package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ExamSite;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExamSiteService extends IService<ExamSite> {

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);

    /**
     * 分页查询考点及其统计信息
     * @param current 当前页码
     * @param size 每页数量
     * @param name 考点名称
     * @return 分页后的考点列表
     */
    Page<ExamSite> getExamSitesWithStatisticsPage(Integer current, Integer size , String name);

    ExamSite getByIdWithStatistics(Long id);

    ExamSite getByExamSiteId(Long examSiteId);
}