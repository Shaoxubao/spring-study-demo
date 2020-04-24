package com.baoge.DI_simaple.controller;

import com.baoge.DI_simaple.service.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/7
 */

@Controller
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

}
