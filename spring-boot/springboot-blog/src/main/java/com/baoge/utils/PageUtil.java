package com.baoge.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

public class PageUtil {

    public static PageInfo getResult(Page page, List list, Class target) {
        List result = target != null ? (List) list.stream().map(o -> parseObject(toJSONString(o), target)
        ).collect(Collectors.toList()) : list;
        PageInfo pageInfo = new PageInfo(result);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }
}
