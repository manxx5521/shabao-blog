package com.xiaoshabao.blog.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BolgWebConfig implements WebMvcConfigurer {
	/**
     * 日志拦截器
     */
    @Autowired
    private TokenInterceptor tokenInterceptor;
    
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		 registry.addInterceptor(tokenInterceptor).addPathPatterns("/user*","/user/**","/post/**");
    }
}
