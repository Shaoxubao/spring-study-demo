package com.baoge.service.impl;

import com.baoge.mapper.UserMapper;
import com.baoge.model.User;
import com.baoge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }
}
