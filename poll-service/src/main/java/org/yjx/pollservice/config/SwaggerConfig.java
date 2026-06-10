package org.yjx.pollservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("在线投票系统 API")
                        .description("基于 Spring Boot 3 + MyBatis + Redis 的投票系统")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("全部接口")
                .packagesToScan("org.yjx.pollservice.controller")  // 扫描 Controller 包
                .build();
    }

}
