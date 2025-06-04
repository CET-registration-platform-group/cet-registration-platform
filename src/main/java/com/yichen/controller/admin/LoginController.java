package com.yichen.controller.admin;

import com.yichen.common.Result;
import com.yichen.service.AuthService;
import com.yichen.vo.LoginVO;
import com.yichen.vo.TokenVO;
import com.yichen.vo.UserVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@Api(tags = "后台管理员认证接口", description = "提供后台管理员登录、登出功能")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @ApiOperation(value = "管理员登录", notes = "管理员登录并返回token和用户信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功", response = Result.class),
        @ApiResponse(code = 400, message = "用户名或密码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/login")
    public Result<TokenVO<UserVO>> login(
        @ApiParam(value = "登录信息(包含登录凭证和密码)", required = true)
        @RequestBody LoginVO loginVO
    ) {
        TokenVO<UserVO> tokenVO = authService.login(loginVO.getUsername(), loginVO.getPassword());
        if (tokenVO != null) {
            return Result.success(tokenVO);
        }
            return Result.error("用户名或密码错误");
    }

    @ApiOperation(value = "管理员登出", notes = "管理员登出并清除token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登出成功", response = Result.class),
        @ApiResponse(code = 401, message = "未登录或token已过期"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/logout")
    public Result<Void> logout() {
        boolean success = authService.logout();
        return Result.success("登出成功");
    }
} 