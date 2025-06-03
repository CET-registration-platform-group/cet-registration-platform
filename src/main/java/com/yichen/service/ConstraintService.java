package com.yichen.service;

import com.yichen.entity.ExamSeat;

/**
 * 实体逻辑约束服务接口
 * 处理实体间的逻辑外键约束关系
 */
public interface ConstraintService {
    
    /**
     * 检查考点是否可以删除
     * @param examSiteId 考点ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteExamSite(Long examSiteId);
    
    /**
     * 检查考场是否可以删除
     * @param examRoomId 考场ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteExamRoom(Long examRoomId);
    
    /**
     * 检查座位是否可以删除
     * @param examSeatId 座位ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteExamSeat(Long examSeatId);
    
    /**
     * 检查座位是否有已分配的考试信息
     * @param examSeatId 座位ID
     * @return 如果没有已分配的考试信息返回true，否则返回false
     */
    boolean hasNoActiveExams(Long examSeatId);
    
    /**
     * 检查用户是否可以删除
     * @param userId 用户ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteUser(Long userId);
    
    /**
     * 检查学生是否可以删除
     * @param studentId 学生ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteStudent(Long studentId);
    
    /**
     * 检查学生是否有已分配的考试信息
     * @param studentId 学生ID
     * @return 如果没有已分配的考试信息返回true，否则返回false
     */
    boolean studentHasNoActiveExams(Long studentId);
    
    /**
     * 根据证件号检查学生是否有已分配的考试信息
     * @param identityDocumentNumber 证件号
     * @return 如果没有已分配的考试信息返回true，否则返回false
     */
    boolean identityDocumentNumberHasNoActiveExams(String identityDocumentNumber);
    
    /**
     * 检查证件号是否唯一
     * @param identityDocumentNumber 证件号
     * @param excludeStudentId 排除的学生ID（可选，用于更新时排除自身）
     * @return 如果证件号唯一返回true，否则返回false
     */
    boolean isIdentityDocumentNumberUnique(String identityDocumentNumber, Long excludeStudentId);

    /**
     * 根据考场ID获取座位号
     */
    String getSeatNumberByExamRoomId(ExamSeat examSeat);
} 