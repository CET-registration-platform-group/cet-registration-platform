package com.yichen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Knife4j配置类
 */
@Configuration
public class Knife4jConfig {

    @Value("${knife4j.openapi.title}")
    private String title;

    @Value("${knife4j.openapi.description}")
    private String description;

    @Value("${knife4j.openapi.email}")
    private String email;

    @Value("${knife4j.openapi.version}")
    private String version;

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台管理接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yichen.controller.admin"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket studentApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("前台学生接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yichen.controller.student"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .contact(new Contact("Admin", "", email))
                .version(version)
                .build();
    }
} 