package com.yichen;

import com.yichen.entity.Student;
import com.yichen.mapper.StudentMapper;
import com.yichen.service.StudentService;
import com.yichen.utils.PasswordUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUser {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void test() {
        String password = PasswordUtils.encrypt("admin123");
        System.out.println(password);
        Student student = studentMapper.selectById(1);
        student.setPassword(password);
        studentMapper.updateById(student);
    }
}
