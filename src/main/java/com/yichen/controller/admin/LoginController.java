package com.yichen.controller.admin;

import com.yichen.common.Result;
import com.yichen.service.LoginService;
import com.yichen.vo.LoginVO;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "用户认证接口", description = "提供后台用户登录、登出功能")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @ApiOperation(value = "用户登录", notes = "用户登录并返回token和用户信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功", response = Result.class),
        @ApiResponse(code = 400, message = "用户名或密码错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/login")
    public Result<LoginVO> login(
        @ApiParam(value = "登录信息(包含登录凭证和密码)", required = true)
        @RequestBody LoginVO loginVO
    ) {
        return loginService.login(loginVO);
    }

    @ApiOperation(value = "用户登出", notes = "用户登出并清除token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登出成功", response = Result.class),
        @ApiResponse(code = 401, message = "未登录或token已过期"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    @PostMapping("/logout")
    public Result<Void> logout(
        @ApiParam(value = "认证令牌(请求头中的Authorization字段)", required = true)
        @RequestHeader("Authorization") String token
    ) {
        return loginService.logout(token);
    }
} 