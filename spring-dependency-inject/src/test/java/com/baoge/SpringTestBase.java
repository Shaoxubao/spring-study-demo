package com.baoge;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * 需要用到spring类测试时继承此类，自动加载spring相关的配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/bean-constructor-inject.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
                         DirtiesContextTestExecutionListener.class})
public abstract class SpringTestBase {

    private static final String ENV = "dev";

    @BeforeClass
    public static void beforeClass(){
        // 设置测试环境变量
        System.setProperty("env", ENV);
        System.out.println("set env to " + ENV);
    }
}
