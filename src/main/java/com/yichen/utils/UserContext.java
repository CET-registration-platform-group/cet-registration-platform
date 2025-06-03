package com.yichen.utils;

import com.yichen.entity.Student;
import com.yichen.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用户上下文，存储当前线程的用户信息（学生或管理员）
 */
@Component
public class UserContext {
    
    private static final ThreadLocal<Student> currentStudent = new ThreadLocal<>();
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    // 学生相关方法
    public Student getCurrentStudent() {
        return currentStudent.get();
    }
    
    public void setCurrentStudent(Student student) {
        currentStudent.set(student);
    }
    
    public Long getCurrentStudentId() {
        return getCurrentStudent() != null ? getCurrentStudent().getId() : null;
    }
    
    public String getCurrentStudentName() {
        return getCurrentStudent() != null ? getCurrentStudent().getName() : null;
    }
    
    // 管理员相关方法
    public User getCurrentUser() {
        return currentUser.get();
    }
    
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    public Long getCurrentUserId() {
        return getCurrentUser() != null ? getCurrentUser().getId() : null;
    }
    
    public String getCurrentUsername() {
        return getCurrentUser() != null ? getCurrentUser().getUsername() : null;
    }
    
    // 判断当前用户类型
    public boolean isAdmin() {
        return getCurrentUser() != null;
    }
    
    public boolean isStudent() {
        return getCurrentStudent() != null;
    }
    
    // 清理上下文
    public void clear() {
        currentStudent.remove();
        currentUser.remove();
    }
} 