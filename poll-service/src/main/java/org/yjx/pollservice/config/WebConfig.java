package org.yjx.pollservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yjx.pollservice.interceptor.TokenInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")              // 拦截所有 /api/ 开头的
                .excludePathPatterns(                    // 排除白名单
                        "/api/user/login",               // 登录不拦
                        "/api/user/register"             // 注册不拦
                );
    }
}
