package com.baoge.service;

import com.baoge.entity.ConsCurve;
import com.baoge.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserDO> {

    UserDO getByUsername(String username, int type);

    ConsCurve find(String consNo, String date);

}