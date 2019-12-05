package com.sy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//允许在指定的类中使用Swagger提供的注解
//容器会自动扫描指定Swagger注解，并生成接口文档
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定要生成接口文档的Controller所在的包
                .apis(RequestHandlerSelectors.basePackage("com.sy.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //接口文档的标题
                .title("论坛前端RESTful接口文档")
                //接口文档的简介
                .description("论坛各个模块功能简介")
                // 服务条款网址
                .termsOfServiceUrl("http://blog.csdn.net/forezp")
                //接口的版本号
                .version("v1.0")
                // 许可证
                .license("苏LP12345")
                //联系方式：公司名称、官网地址、联系用的邮箱
                .contact(new Contact("CSDN论坛", "http://www.sy.com", "xxx@sy.com"))
                .build();
    }
}