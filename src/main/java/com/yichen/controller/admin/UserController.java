package com.yichen.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.User;
import com.yichen.service.UserService;
import com.yichen.common.Result;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户管理", description = "提供管理员用户的增删改查功能")
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "分页查询用户", notes = "分页获取用户列表，支持按用户名模糊查询")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "当前页码(从1开始)", required = true, paramType = "query", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "size", value = "每页数量", required = true, paramType = "query", dataType = "int", example = "10"),
        @ApiImplicitParam(name = "username", value = "用户名(支持模糊查询)", required = false, paramType = "query", dataType = "string")
    })
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping
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

    @ApiOperation(value = "获取用户详情", notes = "根据ID获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "1")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功"),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(user);
    }

    @ApiOperation(value = "添加用户", notes = "添加新的管理员用户")
    @ApiParam(value = "用户信息", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "添加成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping
    public Result<Void> save(@RequestBody User user) {
        boolean success = userService.save(user);
        if (!success) {
            return Result.error(500, "添加失败");
        }
        return Result.success("添加成功");
    }

    @ApiOperation(value = "更新用户", notes = "更新已有的管理员用户信息")
    @ApiParam(value = "用户信息", required = true)
    @ApiResponses({
        @ApiResponse(code = 200, message = "修改成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        if (user.getId() == null) {
            return Result.error(400, "用户ID不能为空");
        }
        
        if (userService.getById(user.getId()) == null) {
            return Result.error(404, "用户不存在");
        }
        
        boolean success = userService.updateById(user);
        if (!success) {
            return Result.error(500, "修改失败");
        }
        return Result.success("修改成功");
    }

    @ApiOperation(value = "删除用户", notes = "根据ID删除管理员用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "long", example = "1")
    @ApiResponses({
        @ApiResponse(code = 200, message = "删除成功"),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (userService.getById(id) == null) {
            return Result.error(404, "用户不存在");
        }
        
        boolean success = userService.removeById(id);
        if (!success) {
            return Result.error(500, "删除失败");
        }
        return Result.success("删除成功");
    }
} 