package com.decoplants.sistema_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//Implementa WebMvcConfigurer para sobreescribir las reglas de rutas web de Spring Boot.
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/recursos/**")
                .addResourceLocations("file:uploads/");
    }
}