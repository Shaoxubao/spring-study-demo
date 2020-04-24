package com.baoge;

import com.baoge.bean.*;
import com.baoge.config.*;
import com.baoge.DI_simaple.service.BookServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/7
 */
public class IOCTest {

    @Test
    public void testBeanByXml() {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

    }

    @Test
    public void testBeanByConfiguration() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = applicationContext.getBean(Person.class);
        System.out.println(person);

        String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : namesForType) {
            System.out.println(name);
        }
    }

    @Test
    public void testFilter() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

    }

    /**
     * 作用域
     */
    @Test
    public void testScope() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println("IOC容器创建完成=======");

        Object person = applicationContext.getBean("person");
        Object person2 = applicationContext.getBean("person");
        System.out.println(person == person2);
    }
    
    @Test
    public void testConditional() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);

        // 获取环境变量
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("os.name");
        System.out.println(property);

        String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : namesForType) {
            System.out.println(name);
        }

        Map<String, Person> map = applicationContext.getBeansOfType(Person.class);
        System.out.println(map);
    }

    @Test
    public void testImport() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        // 工厂bean获取的是调用getObject创建的对象
        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        Object colorFactoryBean2 = applicationContext.getBean("colorFactoryBean");
        System.out.println("bean2类型：" + colorFactoryBean.getClass());
        System.out.println("bean是否单例：" + (colorFactoryBean == colorFactoryBean2));

        // 要获取工厂Bean本身
        Object colorFactoryBean3 = applicationContext.getBean("&colorFactoryBean");
        System.out.println("bean3类型：" + colorFactoryBean3.getClass());

    }

    /**
     * bean生命周期
     */
    @Test
    public void testBeanLifeCycle() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
        System.out.println("容器创建完成...");

//        applicationContext.getBean("car");

        // 关闭容器
        applicationContext.close();
    }

    @Test
    public void testPropertyValue() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigPropertyValue.class);
        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        // 从环境变量中获取
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("person.nickName");
        System.out.println(property);

    }


    @Test
    public void testAutowired() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);
        BookServiceImpl bookService = applicationContext.getBean(BookServiceImpl.class);
        System.out.println(bookService);

//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);


        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);

        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);

        Color color = applicationContext.getBean(Color.class);
        System.out.println(color);


    }

    /**
     * 1、使用命令行动态参数: 在虚拟机参数位置加载 -Dspring.profiles.active=test
     * 2、代码的方式激活某种环境；
     */
    @Test
    public void testProfile() {
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);
//
//        String[] namesForType = applicationContext.getBeanNamesForType(DataSource.class);
//        for (String name : namesForType) {
//            System.out.println(name);
//        }


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 1、创建一个applicationContext
        // 2、设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");
        // 3、注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        //4、启动刷新容器
        applicationContext.refresh();

        String[] namesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String string : namesForType) {
            System.out.println(string);
        }

        Yellow bean = applicationContext.getBean(Yellow.class);
        System.out.println(bean);
        applicationContext.close();


    }

}
