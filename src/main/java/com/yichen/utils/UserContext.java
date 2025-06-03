package com.yichen.utils;

import com.yichen.entity.Student;
import org.springframework.stereotype.Component;

/**
 * 学生上下文，存储当前线程的学生信息
 */
@Component
public class UserContext {
    
    private static final ThreadLocal<Student> currentStudent = new ThreadLocal<>();
    
    public Student getCurrentStudent() {
        return currentStudent.get();
    }
    
    public void setCurrentStudent(Student student) {
        currentStudent.set(student);
    }
    
    public void clear() {
        currentStudent.remove();
    }
    
    public Long getCurrentStudentId() {
        return getCurrentStudent() != null ? getCurrentStudent().getId() : null;
    }
    
    public String getCurrentStudentName() {
        return getCurrentStudent() != null ? getCurrentStudent().getName() : null;
    }
} 