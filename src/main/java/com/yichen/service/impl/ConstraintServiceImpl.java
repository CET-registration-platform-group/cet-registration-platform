package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.entity.*;
import com.yichen.mapper.*;
import com.yichen.service.ConstraintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 实体逻辑约束服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConstraintServiceImpl implements ConstraintService {

    private final ExamRoomMapper examRoomMapper;
    private final ExamSeatMapper examSeatMapper;
    private final ExamInfoMapper examInfoMapper;
    private final ExamSiteMapper examSiteMapper;
    private final StudentMapper studentMapper;

    @Override
    public boolean canDeleteExamSite(Long examSiteId) {
        // 检查是否有关联的考场
        LambdaQueryWrapper<ExamRoom> roomQuery = new LambdaQueryWrapper<>();
        roomQuery.eq(ExamRoom::getExamSiteId, examSiteId);
        return examRoomMapper.selectCount(roomQuery) == 0;
    }

    @Override
    public boolean canDeleteExamRoom(Long examRoomId) {
        // 检查是否有关联的座位
        LambdaQueryWrapper<ExamSeat> seatQuery = new LambdaQueryWrapper<>();
        seatQuery.eq(ExamSeat::getExamRoomId, examRoomId);
        return examSeatMapper.selectCount(seatQuery) == 0;
    }

    @Override
    public boolean canDeleteExamSeat(Long examSeatId) {
        // 检查是否有关联的考试信息
        LambdaQueryWrapper<ExamInfo> examQuery = new LambdaQueryWrapper<>();
        examQuery.eq(ExamInfo::getExamSeatId, examSeatId);
        return examInfoMapper.selectCount(examQuery) == 0;
    }

    @Override
    public boolean hasNoActiveExams(Long examSeatId) {
        // 检查座位是否有分配的考试信息
        LambdaQueryWrapper<ExamInfo> examQuery = new LambdaQueryWrapper<>();
        examQuery.eq(ExamInfo::getExamSeatId, examSeatId);
        return examInfoMapper.selectCount(examQuery) == 0;
    }

    @Override
    public boolean canDeleteUser(Long userId) {
        // 检查是否有关联的学生
        LambdaQueryWrapper<Student> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(Student::getId, userId);
        return studentMapper.selectCount(studentQuery) == 0;
    }

    @Override
    public boolean canDeleteStudent(Long studentId) {
        // 检查是否有关联的考试信息
        LambdaQueryWrapper<ExamInfo> examQuery = new LambdaQueryWrapper<>();
        examQuery.eq(ExamInfo::getStudentId, studentId);
        return examInfoMapper.selectCount(examQuery) == 0;
    }

    @Override
    public boolean studentHasNoActiveExams(Long studentId) {
        // 检查学生是否有分配的考试信息
        LambdaQueryWrapper<ExamInfo> examQuery = new LambdaQueryWrapper<>();
        examQuery.eq(ExamInfo::getStudentId, studentId);
        return examInfoMapper.selectCount(examQuery) == 0;
    }

    @Override
    public boolean identityDocumentNumberHasNoActiveExams(String identityDocumentNumber) {
        // 根据证件号查找学生
        LambdaQueryWrapper<Student> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(Student::getIdentityDocumentNumber, identityDocumentNumber);
        Student student = studentMapper.selectOne(studentQuery);
        
        // 如果学生不存在，则没有考试记录
        if (student == null) {
            return true;
        }
        
        // 检查该学生是否有分配的考试信息
        return studentHasNoActiveExams(student.getId());
    }

    @Override
    public boolean isIdentityDocumentNumberUnique(String identityDocumentNumber, Long excludeStudentId) {
        LambdaQueryWrapper<Student> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(Student::getIdentityDocumentNumber, identityDocumentNumber);
        
        // 如果提供了排除ID，排除自身
        if (excludeStudentId != null) {
            studentQuery.ne(Student::getId, excludeStudentId);
        }
        
        return studentMapper.selectCount(studentQuery) == 0;
    }

    @Override
    public String getSeatNumberByExamRoomId(ExamSeat examSeat) {
        if (examSeat == null || examSeat.getExamRoomId() == null) {
            return null;
        }
        
        ExamRoom examRoom = examRoomMapper.selectById(examSeat.getExamRoomId());
        if (examRoom == null) {
            return examSeat.getSeatNumber();
        }
        
        // 返回格式：考场号-座位号，如 A101-025
        return examRoom.getRoomNumber() + "-" + examSeat.getSeatNumber();
    }
} 