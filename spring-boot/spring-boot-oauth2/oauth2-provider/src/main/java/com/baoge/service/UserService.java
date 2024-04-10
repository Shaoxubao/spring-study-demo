package com.baoge.service;

import com.baoge.model.User;

public interface UserService {
    User findByAccount(String account);
}
