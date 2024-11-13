package com.baoge.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class MybatisPlusConfig {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;


    /**
     * 只会执行一次
     * 顺序：Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void addMysqlInterceptor() {
        // 将mybatis拦截器，添加到chain的最后面
        PaginationInterceptor mybatisInterceptor = new PaginationInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            // 自己添加
            configuration.addInterceptor(mybatisInterceptor);
        }
    }
}

