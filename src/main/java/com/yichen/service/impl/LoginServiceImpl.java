package com.yichen.service.impl;

import com.yichen.common.Result;
import com.yichen.service.LoginService;
import com.yichen.vo.LoginVO;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public Result<LoginVO> login(LoginVO loginVO) {
        // TODO: 实现登录逻辑
        return Result.error("登录功能尚未实现");
    }

    @Override
    public Result<Void> logout(String token) {
        // TODO: 实现登出逻辑
        return Result.error("登出功能尚未实现");
    }
} 