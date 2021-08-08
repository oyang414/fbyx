package com.ouyang.fbyx.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description swagger ui基本配置类
 * @author ouyangxingjie
 * @date 2021/6/29 22:26
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public SwaggerConfig() {
    }

   /**
    * @param
    * @description swagger2的相关配置
    * @return springfox.documentation.spring.web.plugins.Docket
    * @author ouyangxingjie
    * @date 2021/6/29 22:55
    */
    @Bean
    public static Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ouyang.fbyx.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("风暴英雄傻逼花名册api")
                        .description("这是专门用来记录在风暴英雄这款游戏中遇到傻逼的api")
                        .version("1.0")
                        .contact(new Contact("Derlo","","o_yang1993@163.com"))
                        .build());
    }



}

