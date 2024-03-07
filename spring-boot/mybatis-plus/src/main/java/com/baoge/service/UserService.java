package com.baoge.service;

import com.baoge.entity.ConsCurve10;
import com.baoge.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserDO> {

    UserDO getByUsername(String username, int type);

    ConsCurve10 find(String consNo, String date);

}