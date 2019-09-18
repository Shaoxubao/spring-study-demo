package com.baoge.service;

import com.baoge.DI_simaple.DI.Inject;
import com.baoge.dao.ClassDao;
import com.baoge.dao.UserDao;

/**
 * @Author shaoxubao
 * @Date 2019/9/18 17:55
 */
public class UserService {

    @Inject
    private UserDao userDao;

    @Inject
    private ClassDao classDao;

    public void save() {
        userDao.save();
        classDao.save();
    }

    public UserService() {
    }

}
