package com.baoge.config;

import com.baoge.bean.Yellow;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 *
 *
 * Profile：
 * 		Spring为我们提供的可以根据当前环境，动态的激活和切换一系列组件的功能；
 *
 * 开发环境、测试环境、生产环境；
 * 数据源：(/A)(/B)(/C)；
 *
 *
 * @Profile：指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何环境下都能注册这个组件
 *
 * 1）、加了环境标识的bean，只有这个环境被激活的时候才能注册到容器中。默认是default环境
 * 2）、写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效
 * 3）、没有标注环境标识的bean在，任何环境下都是加载的；
 */

@Profile("dev")
@PropertySource("classpath:/dbconfig.properties")
@Configuration
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    private StringValueResolver stringValueResolver;

    @Value("${db.user}")
    private String user;

    private String driverClass;

//    @Profile("dev") // @Profile标注在类上，这里可以不用@Profile
    @Bean
    public Yellow yellow() {
        return new Yellow();
    }

//    @Profile("dev")
//    @Profile("default")
    @Bean("devDataSource")
    public DataSource dataSourceDev(@Value("${db.password}") String password) throws Exception {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        comboPooledDataSource.setDriverClass(driverClass);

        return comboPooledDataSource;
    }

    @Profile("test")
    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String password) throws Exception {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/dbblog");
        comboPooledDataSource.setDriverClass(driverClass);

        return comboPooledDataSource;
    }

    @Profile("prop")
    @Bean("propDataSource")
    public DataSource dataSourceProp(@Value("${db.password}") String password) throws Exception {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setUser(user);
        comboPooledDataSource.setPassword(password);
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/springbootv2");
        comboPooledDataSource.setDriverClass(driverClass);

        return comboPooledDataSource;
    }

    /**
     * Set the StringValueResolver to use for resolving embedded definition values.
     *
     * @param resolver
     */
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
        driverClass = stringValueResolver.resolveStringValue("${db.driverClass}");
    }
}
