package com.happysnaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/10
 * @email happysnaker@foxmail.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Happysnaker")
                .apiInfo(new ApiInfo("Happysnaker的文档接口",
                        "这是Happysnaker的文档接口",
                        "1.0",
                        "https://happysnaker.xyz",
                        new Contact("Happysnaker",
                                "https://happysnaker.xyz",
                                "1637318597@qq.com"),
                        "暂无",
                        "10",
                        new ArrayList()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.happysnaker.controller"))
                .build();
    }


}
