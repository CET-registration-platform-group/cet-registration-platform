package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.User;
import com.yichen.service.UserService;
import com.yichen.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {
        
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(username != null, User::getUsername, username);
        
        userService.page(page, queryWrapper);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PostMapping
    public Result save(@RequestBody User user) {
        userService.save(user);
        return Result.success("添加成功");
    }

    @PutMapping
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
} 