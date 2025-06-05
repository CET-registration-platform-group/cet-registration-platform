package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ExamSeat;
import com.yichen.entity.Student;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ExamInfoMapper;
import com.yichen.entity.ExamInfo;
import com.yichen.service.ExamInfoService;
import com.yichen.service.ExamSeatService;
import com.yichen.service.StudentService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 考试信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class ExamInfoServiceImpl extends ServiceImpl<ExamInfoMapper, ExamInfo> implements ExamInfoService {

    private final StudentService studentService;
    private final ExamSeatService examSeatService;

    @Override
    public Page<ExamInfo> listExamInfos(Integer current, Integer size, Long studentId, Long examSeatId, String examType, String examLevel) {
        Page<ExamInfo> page = new Page<>(current, size);
        LambdaQueryWrapper<ExamInfo> queryWrapper = new LambdaQueryWrapper<>();
        
        if (studentId != null) {
            queryWrapper.eq(ExamInfo::getStudentId, studentId);
        }
        
        if (examSeatId != null) {
            queryWrapper.eq(ExamInfo::getExamSeatId, examSeatId);
        }
        
        if (examType != null && !examType.isEmpty()) {
            queryWrapper.eq(ExamInfo::getExamType, examType);
        }
        
        if (examLevel != null && !examLevel.isEmpty()) {
            queryWrapper.eq(ExamInfo::getExamLevel, examLevel);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
    
    /**
     * 检查学生是否已经报名
     * @param studentId 学生ID
     * @return 如果学生已经报名返回true，否则返回false
     */
    private boolean isStudentRegistered(Long studentId) {
        LambdaQueryWrapper<ExamInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExamInfo::getStudentId, studentId);
        return count(queryWrapper) > 0;
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ExamInfo entity) {
        // 检查学生是否存在
        Long studentId = entity.getStudentId();
        if (studentId != null) {
            Student student = studentService.getById(studentId);
            BusinessValidationUtil.checkNotNull(student, "关联的学生不存在");
            
            // 检查学生是否已经报名（排除当前记录）
            if (entity.getId() != null) {
                LambdaQueryWrapper<ExamInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ExamInfo::getStudentId, studentId)
                          .ne(ExamInfo::getId, entity.getId());
                BusinessValidationUtil.check(count(queryWrapper) == 0, "该学生已经报名，不能重复报名");
            }
        }
        
        // 检查座位是否存在且可用
        Long examSeatId = entity.getExamSeatId();
        if (examSeatId != null) {
            ExamSeat examSeat = examSeatService.getById(examSeatId);
            BusinessValidationUtil.checkNotNull(examSeat, "关联的座位不存在");
            
            // 检查座位是否已被占用（排除当前记录）
            if (entity.getId() != null) {
                LambdaQueryWrapper<ExamInfo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ExamInfo::getExamSeatId, examSeatId)
                          .ne(ExamInfo::getId, entity.getId());
                BusinessValidationUtil.check(count(queryWrapper) == 0, "该座位已被占用");
            } else {
                BusinessValidationUtil.check(examSeat.getStatus() == 0, "该座位已被占用");
            }
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ExamInfo entity) {
        // 检查学生是否存在
        Long studentId = entity.getStudentId();
        if (studentId != null) {
            Student student = studentService.getById(studentId);
            BusinessValidationUtil.checkNotNull(student, "关联的学生不存在");
            
            // 检查学生是否已经报名
            BusinessValidationUtil.check(!isStudentRegistered(studentId), "该学生已经报名，不能重复报名");
        }
        
        // 检查座位是否存在且可用
        Long examSeatId = entity.getExamSeatId();
        if (examSeatId != null) {
            ExamSeat examSeat = examSeatService.getById(examSeatId);
            BusinessValidationUtil.checkNotNull(examSeat, "关联的座位不存在");
            
            // 检查座位状态是否可用
            BusinessValidationUtil.check(examSeat.getStatus() == 0, "该座位已被占用");
        
            // 更新座位状态为已占用
            examSeat.setStatus(1);
            examSeatService.updateById(examSeat);
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        // 获取考试信息
        ExamInfo examInfo = getById(id);
        if (examInfo == null) {
            return false;
        }
        
        // 删除考试信息，并释放座位
        boolean result = super.removeById(id);
        if (result && examInfo.getExamSeatId() != null) {
            ExamSeat examSeat = examSeatService.getById(examInfo.getExamSeatId());
            if (examSeat != null) {
                examSeat.setStatus(0); // 设置座位状态为未占用
                examSeatService.updateById(examSeat);
            }
        }
        
        return result;
    }
} 