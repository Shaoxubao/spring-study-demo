package com.baoge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baoge.common.Result;
import com.baoge.entity.User;
import com.baoge.mapper.UserMapper;
import com.baoge.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoge.util.BcryptUtil;
import com.baoge.util.JwtUtil;
import com.baoge.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Result login(String username, String password) {

        // 先从数据库查询
        User user = this.getOne(new QueryWrapper<User>().eq("username", username));
        if (null == user) {
            Result.fail("用户不存在");
        }
        if (!BcryptUtil.match(password, user.getPassword())) {
            return Result.fail("密码错误");
        }
        String jwtToken = jwtUtil.createJwtToken(user.getId().toString(), 10 * 10);
        redisUtil.set("token_" + jwtToken, user, 60 * 10, TimeUnit.SECONDS);
        return Result.success(jwtToken);
    }
}
