package com.navis.UserDetails.config;

import com.navis.UserDetails.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
public class UserInterceptConfig extends WebMvcConfigurationSupport {

    @Autowired
    UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(userInterceptor);
    }
}
