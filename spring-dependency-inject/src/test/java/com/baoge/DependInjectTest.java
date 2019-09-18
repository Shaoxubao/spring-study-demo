package com.baoge;

import com.baoge.constructorinject.InterfaceA;
import com.baoge.constructorinject.config.MainConfig;
import com.baoge.setterinject.InterfaceC;
import com.baoge.setterinject.InterfaceD;
import com.baoge.setterinject.config.MyConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/14
 */

public class DependInjectTest /** extends SpringTestBase */ {

    /**
     * 构造注入
     */
    @Test
    public void testConstructorInjection() {

        // xml
//        ApplicationContext context = new ClassPathXmlApplicationContext("bean-constructor-inject.xml");
//        InterfaceA a2 = (InterfaceA) context.getBean("interfaceA");
//        System.out.println(a2);

        // annotation
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        InterfaceA a = (InterfaceA) applicationContext.getBean("interfaceA");
        System.out.println(a);

    }

    /**
     * setter注入
     */
    @Test
    public void testSetterInject() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfig.class);

        InterfaceC c = (InterfaceC) applicationContext.getBean("interfaceC");
        System.out.println(c.callInterfaceD());

        InterfaceD d = (InterfaceD) applicationContext.getBean("interfaceD");
        System.out.println(d.callInterfaceC());
    }

}
