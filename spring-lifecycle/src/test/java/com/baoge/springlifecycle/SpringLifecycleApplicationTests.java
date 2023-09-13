package com.baoge.springlifecycle;

import com.baoge.springlifecycle.config.SpringConfig;
import com.baoge.springlifecycle.lifecycle.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SpringLifecycleApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
class SpringLifecycleApplicationTests {

    @Test
    void contextLoads() {
        log.info("contextLoads is running");
    }

    @Test
    void testLifecycle() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        User user = context.getBean(User.class);
        System.out.println(user);
    }

}
