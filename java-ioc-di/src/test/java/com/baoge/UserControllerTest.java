package com.baoge;

import com.baoge.DI.BeanFactory;
import com.baoge.controller.UserController;

/**
 * @Author shaoxubao
 * @Date 2019/9/18 17:57
 */
public class UserControllerTest {

    public static void main(String[] args) {

        UserController userController = BeanFactory.getBean(UserController.class);
        userController.save();

    }

}
