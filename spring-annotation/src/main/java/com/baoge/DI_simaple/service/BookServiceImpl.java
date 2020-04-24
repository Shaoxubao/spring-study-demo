package com.baoge.DI_simaple.service;

import com.baoge.DI_simaple.dao.BookDao;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/7
 */

@Service
public class BookServiceImpl {

//    @Qualifier("bookDao2")
//    @Autowired
//    @Resource(name = "bookDao2")
    @Inject
    private BookDao bookDao;

    @Override
    public String toString() {
        return "BookServiceImpl{" +
                "bookDao=" + bookDao +
                '}';
    }
}
