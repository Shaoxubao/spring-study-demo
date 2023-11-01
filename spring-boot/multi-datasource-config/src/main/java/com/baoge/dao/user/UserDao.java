package com.baoge.dao.user;

import com.baoge.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User selectByPrimaryKey(Long id);
}
