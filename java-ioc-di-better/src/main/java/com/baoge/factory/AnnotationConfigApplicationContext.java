package com.baoge.factory;

import com.baoge.annotation.MyAutowired;
import com.baoge.annotation.MyComponent;
import com.baoge.annotation.MyValue;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2018-2028 Baoge All Rights Reserved.
 * Author: Shao Xu Bao <15818589952@163.com>
 * Date:   2019/9/18
 */
public class AnnotationConfigApplicationContext {

    /**此Map容器用于存储类定义对象*/
    private Map<String, Class<?>> beanDefinitionFactory = new ConcurrentHashMap<String, Class<?>>();
    /**此Map容器用于存储单例对象*/
    private Map<String,Object> singletonBeanFactory = new ConcurrentHashMap<String, Object>();

    /**有参构造方法,参数类型为指定要扫描加载的包名,此工厂可接收多个包路径*/
    public AnnotationConfigApplicationContext(String... packageNames) {
        // 遍历扫描指定的所有包路径
        for (String packageName : packageNames) {
            System.out.println("开始扫描包:" + packageName);
            /**扫描指定的包路径*/
            scanPkg(packageName);
        }
        /**进行DI依赖注入*/
        dependencyInjection();
    }

    /**
     * 扫描指定包,找到包中的类文件。
     * 对于标准(类上有定义注解的)类文件反射加载创建类定义对象并放入容器中
     */
    private void scanPkg(final String pkg) {
        // 替换包名中的".",将包结构转换为目录结构
        String pkgDir = pkg.replaceAll("\\.", "/");
        // 获取目录结构在类路径中的位置(其中url中封装了具体资源的路径)
        URL url = getClass().getClassLoader().getResource(pkgDir);
        // 基于这个路径资源(url),构建一个文件对象
        File file = new File(url.getFile());
        // 获取此目录中指定标准(以".class"结尾)的文件
        File[] fs = file.listFiles(new FileFilter() {
            public boolean accept(File file) {
                // 获取文件名
                String fName = file.getName();
                // 判断该文件是否为目录，如为目录，递归进一步扫描其内部所有文件
                if(file.isDirectory()) {
                    scanPkg(pkg + "." + fName);
                } else {
                    // 判定文件的后缀是否为.class
                    if(fName.endsWith(".class")) {
                        return true;
                    }
                }
                return false;
            }
        });
        // 遍历所有符合标准的File文件
        for(File f : fs) {
            // 获取文件名
            String fName = f.getName();
            // 获取去除.class之后的文件名
            fName = fName.substring(0, fName.lastIndexOf("."));
            // 将名字(类名,通常为大写开头)的第一个字母转换小写(用它作为key存储工厂中)
            String beanId = String.valueOf(fName.charAt(0)).toLowerCase() + fName.substring(1);
            // 构建一个类全名(包名.类名)
            String pkgCls=pkg+"." + fName;
            try {
                // 通过反射构建类对象
                Class<?> c = Class.forName(pkgCls);
                // 判定这个类上是否有MyComponent注解
                if(c.isAnnotationPresent(MyComponent.class)) {
                    // 将类对象存储到map容器中
                    beanDefinitionFactory.put(beanId, c);
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 此方法用于对属性进行依赖注入。
     * 从工厂中获取所有的类对象，如果类中的属性上有MyAutowired注解，
     * 那么首先从根据属性名从工厂中获取对象，或者根据对象类型获取对象。
     * 最后用该对象对属性进行注入。
     */
    private void dependencyInjection(){
        // 获取容器中所有的类定义对象
        Collection<Class<?>> classes = beanDefinitionFactory.values();
        // 遍历每一个类对象
        for (Class<?> cls : classes) {
            // 获取类对象的名字全称(包名+类名)
            String clsName = cls.getName();
            // 获取类名
            clsName = clsName.substring(clsName.lastIndexOf(".") + 1);
            // 将类名(通常为大写开头)的第一个字母转换小写
            String beanId=String.valueOf(clsName.charAt(0)).toLowerCase() + clsName.substring(1);
            // 获取类中所有的属性
            Field[] fields = cls.getDeclaredFields();
            // 遍历每一个属性
            for (Field field : fields) {
                // 如果这个属性上有MyAutowired注解，进行注入操作
                if(field.isAnnotationPresent(MyAutowired.class)) {
                    try {
                        // 获取属性名
                        String fieldName = field.getName();
                        System.out.println("属性名:"+fieldName);
                        // 定义为属性注入的bean对象(此对象从容器中获取)
                        Object fieldBean = null;
                        // 首先根据属性名从容器中取出对象，如果不为null，则赋值给fieldBean对象
                        if(beanDefinitionFactory.get(fieldName) != null) {
                            fieldBean = getBean(fieldName,field.getType());
                        } else {    // 否则按照属性的类型从容器中取出对象进行注入
                            // 获取属性的类型(包名+类名)
                            String type = field.getType().getName();
                            // 截取最后的类名
                            type = type.substring(type.lastIndexOf(".")+1);
                            // 将类名(通常为大写开头)的第一个字母转换小写
                            String fieldBeanId=String.valueOf(type.charAt(0)).toLowerCase()+type.substring(1);
                            System.out.println("属性类型ID:"+fieldBeanId);
                            // 根据转换后的类型beanId，从容器中获取对象并赋值给fieldBean对象
                            fieldBean = getBean(fieldBeanId,field.getType());
                        }
                        System.out.println("要为属性注入的值:"+fieldBean);
                        // 如果fieldBean对象不为空，则为该属性进行注入
                        if(fieldBean != null) {
                            // 获取此类定义的对象的实例对象
                            Object clsBean = getBean(beanId, cls);
                            // 设置此属性可访问
                            field.setAccessible(true);
                            // 为该属性注入值
                            field.set(clsBean, fieldBean);
                            System.out.println("注入成功!");
                        } else {
                            System.out.println("注入失败!");
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 根据传入的bean的id值获取容器中的对象，类型为Object
     */
    public Object getBean(String beanId) {
        // 根据传入beanId获取类对象
        Class<?> cls = beanDefinitionFactory.get(beanId);
        // 根据类对象获取其定义的注解
        MyComponent annotation = cls.getAnnotation(MyComponent.class);
        // 获取注解的scope属性值
        String scope = annotation.scope();
        try {
            // 如果scope等于singleton,创建单例对象
            if("singleton".equals(scope) || "".equals(scope)) {
                // 判断容器中是否已有该对象的实例,如果没有,创建一个实例对象放到容器中
                if(singletonBeanFactory.get(beanId)==null) {
                    Object instance = cls.newInstance();
                    setFieldValues(cls,instance);
                    singletonBeanFactory.put(beanId,instance);
                }
                // 根据beanId获取对象并返回
                return singletonBeanFactory.get(beanId);
            }
            // 如果scope等于prototype,则创建并返回多例对象
            if("prototype".equals(scope)) {
                Object instance = cls.newInstance();
                setFieldValues(cls,instance);
                return instance;
            }
            // 目前仅支持单例和多例两种创建对象的方式
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 如果遭遇异常，返回null
        return null;
    }
    /**
     * 此为重载方法，根据传入的class对象在内部进行强转，
     * 返回传入的class对象的类型
     */
    public <T>T getBean(String beanId, Class<T> c){
        return (T)getBean(beanId);
    }

    /**
     * 此方法用于为对象的属性赋值
     * 内部是通过获取成员属性上注解的值，在转换为类型之后，通过反射为对象赋值
     * @param cls 类定义对象
     * @param obj 要为其赋值的实例对象
     */
    private void setFieldValues(Class<?> cls,Object obj) {
        // 获取类中所有的成员属性
        Field[] fields = cls.getDeclaredFields();
        // 遍历所有属性
        for (Field field : fields) {
            // 如果此属性有MyValue注解修饰，对其进行操作
            if(field.isAnnotationPresent(MyValue.class)) {
                // 获取属性名
                String fieldName = field.getName();
                // 获取注解中的值
                String value = field.getAnnotation(MyValue.class).value();
                // 获取属性所定义的类型
                String type = field.getType().getName();
                // 将属性名改为以大写字母开头，如：id改为ID，name改为Name
                fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase()+fieldName.substring(1);
                // set方法名称，如：setId,setName...
                String setterName = "set" + fieldName;
                try {
                    // 根据方法名称和参数类型获取对应的set方法对象
                    Method method = cls.getDeclaredMethod(setterName, field.getType());
                    // 判断属性类型，如类型不一致，则转换类型后调用set方法为属性赋值
                    if("java.lang.Integer".equals(type) || "int".equals(type)){
                        int intValue = Integer.valueOf(value);
                        method.invoke(obj, intValue);
                    } else if("java.lang.String".equals(type)){
                        method.invoke(obj, value);
                    }
                    // 作为测试，仅判断Integer和String类型，其它类型同理
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 销毁方法，用于释放资源
     */
    public void close() {
        beanDefinitionFactory.clear();
        beanDefinitionFactory = null;
        singletonBeanFactory.clear();
        singletonBeanFactory = null;
    }

}
