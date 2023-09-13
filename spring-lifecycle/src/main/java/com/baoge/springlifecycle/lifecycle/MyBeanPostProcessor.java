package com.baoge.springlifecycle.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("user")) {
            System.out.println("postProcessBeforeInitialization 方法执行了，user对象初始化之前开始增强...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("user")) {
            System.out.println("postProcessAfterInitialization 方法执行了，user对象初始化之后开始增强...");

            // CGlib代理对象
            Enhancer enhancer = new Enhancer();
            // 设置需要增强的类
            enhancer.setSuperclass(bean.getClass());
            // 执行回调方法，增强方法
            enhancer.setCallback(new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    // 执行目标方法
                    return method.invoke(method, objects);
                }
            });
            // 创建代理对象
            return enhancer.create();
        }
        return bean;
    }
}
