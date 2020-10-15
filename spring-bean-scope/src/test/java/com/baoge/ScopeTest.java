package com.baoge;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <xubao_shao@163.com>
 * Date:   2020/10/15
 */
public class ScopeTest {

    ApplicationContext ioc = new ClassPathXmlApplicationContext("application.xml");

    @Test
    public void testScope() {
        Book book = (Book) ioc.getBean("book");
        Book book2 = (Book) ioc.getBean("book");
//        System.out.println(book + "\n" + book2);
        System.out.println("book: " + book.hashCode() + ", book2: " + book2.hashCode());
        System.out.println(book == book2);
    }
}
