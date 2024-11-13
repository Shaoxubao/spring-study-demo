package com.baoge.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

public interface PageHelperService<T> extends IService<T> {
    PageInfo<T> pageHelper(Integer pageNum, Integer pageSize);

    PageInfo<T> pageHelper(Wrapper<T> queryWrapper, Integer pageNum, Integer pageSize);
}
