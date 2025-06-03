package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ExamInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试信息服务接口
 */
public interface ExamInfoService extends IService<ExamInfo> {
    /**
     * 分页查询考试信息列表，支持按学生ID和考位ID筛选
     * @param current 当前页
     * @param size 每页数量
     * @param studentId 学生ID（可选）
     * @param examSeatId 考位ID（可选）
     * @param examType 考试类型（可选）
     * @param examLevel 考试级别（可选）
     * @return 考试信息分页数据
     */
    Page<ExamInfo> listExamInfos(Integer current, Integer size, Long studentId, Long examSeatId, String examType, String examLevel);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);
} 