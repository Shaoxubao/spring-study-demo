package com.baoge.config;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 *Swagger配置类
 */
@Configuration
public class SwaggerConfigruationSettings{
    // 创建Docket对象
    @Bean
    public OpenAPI springShopOpenApi(){
        Info info = new Info()
                .title("docker项目")
                .version("v1.0")
                .description("study")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://baoge.cn")
                );
 
        return new OpenAPI().info(info).externalDocs(new ExternalDocumentation()
                .description("doc").url("http://baoge.cn"));
    }
}