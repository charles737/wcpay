package com.charli.wcpay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class Cors extends WebMvcConfigurerAdapter {

    /**
     * 进行注册
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有路径跨域处理
                .allowedOrigins("*") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH") // 允许哪些方法进行跨域
                .allowCredentials(true).maxAge(3600); // 允许资格，有效期
    }
}
