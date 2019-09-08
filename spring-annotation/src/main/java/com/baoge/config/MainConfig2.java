package com.baoge.config;

import com.baoge.bean.Color;
import com.baoge.bean.ColorFactoryBean;
import com.baoge.bean.Person;
import com.baoge.bean.Red;
import com.baoge.condition.LinuxCondition;
import com.baoge.condition.WindowsCondition;
import org.springframework.context.annotation.*;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/8
 */

// 类中组件统一设置。满足当前条件，这个类中配置的所有bean注册才能生效；
@Conditional({WindowsCondition.class})
@Configuration
@Import({Color.class, Red.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})  // 导入组件（可以是数组），id默认是全类名
public class MainConfig2 {

    //默认是单实例的
    /**
     * @Scope:调整作用域
     * prototype：多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。
     * 					每次获取的时候才会调用方法创建对象；
     * singleton：单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。
     * 			以后每次获取就是直接从容器（map.get()）中拿，
     * request：同一次请求创建一个实例
     * session：同一个session创建一个实例
     *
     * 懒加载：
     * 		单实例bean：默认在容器启动的时候创建对象；
     * 		懒加载：容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化；
     *
     */
//    @Scope("prototype") // 作用域
    @Lazy               // 懒加载
    @Bean("person")
    public Person person() {
        System.out.println("给容器中添加person========");
        return new Person(25, "小米");
    }

    /**
     * @Conditional({Condition}) ： 按照一定的条件进行判断，满足条件给容器中注册bean
     *
     * 如果系统是windows，给容器中注册("bill")
     * 如果是linux系统，给容器中注册("linux")
     */
//    @Conditional({WindowsCondition.class})
    @Bean("bill")
    public Person person01() {
        return new Person(24,"bill");
    }

    @Conditional(LinuxCondition.class)
    @Bean("linus")
    public Person person02() {
        return new Person(66, "linus");
    }

    /**
     * 给容器中注册组件；
     * 1）、包扫描+组件标注注解（@Controller/@Service/@Repository/@Component）[自己写的类]
     * 2）、@Bean[导入的第三方包里面的组件]
     * 3）、@Import[快速给容器中导入一个组件]
     * 		1）、@Import(要导入到容器中的组件)；容器中就会自动注册这个组件，id默认是全类名
     * 		2）、ImportSelector:返回需要导入的组件的全类名数组；
     * 		3）、ImportBeanDefinitionRegistrar:手动注册bean到容器中
     * 4）、使用Spring提供的 FactoryBean（工厂Bean）;
     * 		1）、默认获取到的是工厂bean调用getObject创建的对象
     * 		2）、要获取工厂Bean本身，我们需要给id前面加一个&，
     * 			即&colorFactoryBean
     */
    @Bean
    public ColorFactoryBean colorFactoryBean() {
        return new ColorFactoryBean();
    }


}
