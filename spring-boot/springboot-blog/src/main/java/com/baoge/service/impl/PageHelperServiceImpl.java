package com.baoge.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageHelperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements com.baoge.service.PageHelperService<T> {

    @Override
    public PageInfo<T> pageHelper(Integer pageNum, Integer pageSize) {
        return pageHelper(null, pageNum, pageSize);
    }

    @Override
    public PageInfo<T> pageHelper(Wrapper<T> queryWrapper, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<T> ts = baseMapper.selectList(queryWrapper);

        return new PageInfo<>(ts);
    }
}
