package com.example.springai_chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类，用于配置Web相关的设置
 * 主要配置跨域资源共享(CORS)策略，允许前端应用从不同源访问后端API
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    /**
     * 配置跨域资源共享(CORS)映射规则
     * 此方法允许所有来源、所有HTTP方法和所有请求头的跨域请求
     * 
     * @param registry CORS注册对象，用于配置跨域规则
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有路径启用CORS
                .allowedOrigins("*")  // 允许所有来源的跨域请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*");  // 允许所有请求头
    }
}