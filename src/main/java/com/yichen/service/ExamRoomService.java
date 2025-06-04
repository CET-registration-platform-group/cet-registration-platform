package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ExamRoom;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考场服务接口
 */
public interface ExamRoomService extends IService<ExamRoom> {
    /**
     * 分页查询考场列表，支持按考场号和考点ID筛选
     * @param current 当前页
     * @param size 每页数量
     * @param roomNumber 考场号（可选）
     * @param examSiteId 考点ID（可选）
     * @return 考场分页数据
     */
    Page<ExamRoom> listExamRooms(Integer current, Integer size, String roomNumber, Long examSiteId);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);

    List<ExamRoom> getByExamSiteId(Long examSiteId);
}