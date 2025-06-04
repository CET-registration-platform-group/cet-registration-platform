package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ExamRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRoomMapper extends BaseMapper<ExamRoom> {
    List<ExamRoom> getByExamSiteId(Long examSiteId);
}
