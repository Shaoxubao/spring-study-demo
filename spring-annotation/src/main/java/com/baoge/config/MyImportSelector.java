package com.baoge.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

// 自定义逻辑返回需要导入的组件
public class MyImportSelector implements ImportSelector {
    /**
     * @param importingClassMetadata
     *
     * 返回值，就是导入到容器的组件全类名
     */
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"com.baoge.bean.Blue", "com.baoge.bean.Yellow"};
    }
}
