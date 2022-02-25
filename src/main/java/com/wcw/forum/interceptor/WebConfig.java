package com.wcw.forum.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).
                addPathPatterns("/admin/**").
//                登入頁面也要排除，不然會一直重導頁面
                excludePathPatterns("/admin").
//                登入頁面form表單送出的請求也要排除，否則表單一送出就被重導到登入頁面
                excludePathPatterns("/admin/login");
    }
}
