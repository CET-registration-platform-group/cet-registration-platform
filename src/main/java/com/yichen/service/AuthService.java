package com.yichen.service;

import com.yichen.vo.TokenVO;

public interface AuthService {
    TokenVO login(String username, String password);
    boolean logout();
} 