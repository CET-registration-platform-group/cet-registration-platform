package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ExamSite;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSiteMapper extends BaseMapper<ExamSite> {
    /**
     * 查询考点及其统计信息
     */
    long countWithStatistics();

    List<ExamSite> selectWithStatisticsPage(int offset, int limit, String name);

    ExamSite getByIdWithStatistics(Long id);

    ExamSite getByExamSiteId(Long examSiteId);
}