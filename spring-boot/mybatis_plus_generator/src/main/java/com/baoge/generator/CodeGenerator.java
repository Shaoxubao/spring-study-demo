package com.baoge.generator;
 
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
 
import java.util.Scanner;
 
public class CodeGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要生成代码的表名，多个表名使用逗号分隔：");
        String tablesInput = scanner.nextLine();
        String[] tables = tablesInput.split(",");
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
 
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("baoge"); // 作者信息：欢迎关注程序员良枫
        gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");//设置生成的路径
        gc.setServiceName("%sService");//去掉服务接口service前缀
        gc.setFileOverride(false);//是否覆盖文件。如果代码直接生成到生产代码里面，请谨慎启用，避免覆盖掉项目代码。
        gc.setOpen(false);//是否打开
        mpg.setGlobalConfig(gc);
 
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        //改成你自己的数据库
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/virtual_power_1125?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");//mysql用户名
        dsc.setPassword("123456");//mysql密码
        mpg.setDataSource(dsc);
 
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null); //必须设置，不然requestMapping会出现双斜杠问题。
        pc.setParent("com.baoge");//包配置
        pc.setController("controller"); //controller
        pc.setService("service"); //服务接口
        pc.setServiceImpl("service.impl"); //服务实现
        pc.setMapper("mapper"); //mapper
        pc.setEntity("entity"); //实体
        mpg.setPackageInfo(pc);
 
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tables); // 从命令行参数读取表名
        mpg.setStrategy(strategy);
 
        mpg.execute();
    }
 
}