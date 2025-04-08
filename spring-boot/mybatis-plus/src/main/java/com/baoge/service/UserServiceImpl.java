package com.baoge.service;

import com.baoge.entity.ConsCurve;
import com.baoge.entity.User;
import com.baoge.entity.UserDO;
import com.baoge.mapper.ConsCurve10Mapper;
import com.baoge.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final ConsCurve10Mapper consCurve10Mapper;

    @Override
    public User getByUsername(String username, int type) {
        if (type == 0) {
            // xml
            log.info("query from xml");
            return userMapper.selectByUsername(username);
        } else {
            // QueryWrapper
            log.info("query from wrapper");
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getUsername, username);
//            queryWrapper.eq(UserDO::getStatus, 1);
            return userMapper.selectOne(queryWrapper);
        }

    }

    @Override
    public ConsCurve find(String consNo, String date) {
        return consCurve10Mapper.find(consNo, date);
    }

}