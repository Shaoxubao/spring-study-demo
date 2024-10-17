package com.baoge;

import com.baoge.entity.User;
import com.baoge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroJwtApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGet() {
        User user = userService.getById("1846746177417048065");
        System.out.println(user);
    }

}
