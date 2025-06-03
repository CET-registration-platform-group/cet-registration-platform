package com.yichen.service;

import com.yichen.vo.TokenVO;
import com.yichen.vo.UserVO;

public interface AuthService {
    TokenVO<UserVO> login(String username, String password);
    boolean logout();
} 