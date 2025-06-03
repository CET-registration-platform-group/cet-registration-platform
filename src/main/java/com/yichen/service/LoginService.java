package com.yichen.service;

import com.yichen.common.Result;
import com.yichen.vo.LoginVO;

public interface LoginService {
    /**
     * 用户登录
     * @param loginVO 登录信息
     * @return 登录结果
     */
    Result<LoginVO> login(LoginVO loginVO);

    /**
     * 用户登出
     * @param token 认证令牌
     * @return 登出结果
     */
    Result<Void> logout(String token);
} 