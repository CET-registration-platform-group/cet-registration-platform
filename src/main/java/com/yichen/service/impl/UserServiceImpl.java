package com.yichen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.User;
import com.yichen.mapper.UserMapper;
import com.yichen.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
} 