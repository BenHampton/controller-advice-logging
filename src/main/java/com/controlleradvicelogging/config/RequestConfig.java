package com.controlleradvicelogging.config;

import com.controlleradvicelogging.interceptors.BodilessRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RequestConfig implements WebMvcConfigurer {

    private final BodilessRequestInterceptor bodilessRequestInterceptor;

    public RequestConfig(BodilessRequestInterceptor bodilessRequestInterceptor) {
        this.bodilessRequestInterceptor = bodilessRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bodilessRequestInterceptor);
    }
}
