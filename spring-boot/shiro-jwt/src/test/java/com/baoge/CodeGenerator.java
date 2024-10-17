package com.baoge;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * 代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        // 连接数据库
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/shiro_jwt?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("豹哥") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            // 设置日期时间
                            .dateType(DateType.ONLY_DATE)
                            .outputDir("D:\\WorkSpace\\idea\\shiro_jwt\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.baoge") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\WorkSpace\\idea\\shiro_jwt\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user") // 设置需要生成的表名
                            .addTablePrefix("t_"); // 设置过滤表前

                    // 新增数据，自动为创建时间赋值
                    IFill createFill = new Column("created_date", FieldFill.INSERT);
                    IFill updateFill = new Column("updated_date", FieldFill.UPDATE);
                    builder.entityBuilder()
                            // 设置id类型
                            .idType(IdType.ASSIGN_ID)
                            // 开启 Lombok
                            .enableLombok()
                            // 开启连续设置模式
                            .enableChainModel()
                            // 驼峰命名模式
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // 自动为创建时间、修改时间赋值
                            .addTableFills(createFill).addTableFills(updateFill)
                            // 逻辑删除字段
                            .logicDeleteColumnName("is_deleted");

                    // Restful 风格
                    builder.controllerBuilder().enableRestStyle();
                    // 去除 Service 前缀的 I
                    builder.serviceBuilder().formatServiceFileName("%sService");
                    // mapper 设置
                    builder.mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                })
                // 固定
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
