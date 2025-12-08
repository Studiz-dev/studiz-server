package com.studiz.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 파일 서빙 설정
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:uploads/");
    }
}







