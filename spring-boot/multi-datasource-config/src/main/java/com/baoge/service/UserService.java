package com.baoge.service;

import com.baoge.dao.order.OrderDao;
import com.baoge.dao.user.UserDao;
import com.baoge.entity.Order;
import com.baoge.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUser() {
        return userDao.selectByPrimaryKey(1L);
    }
}
