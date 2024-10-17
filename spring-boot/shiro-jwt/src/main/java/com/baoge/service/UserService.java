package com.baoge.service;

import com.baoge.common.Result;
import com.baoge.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 服务类
 */
public interface UserService extends IService<User> {

    Result login(String username, String password);

}
