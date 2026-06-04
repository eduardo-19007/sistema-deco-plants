package com.decoplants.sistema_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Recursos Estáticos (Imágenes subidas por el usuario).
 * Implementa WebMvcConfigurer para sobreescribir las reglas de rutas web de Spring Boot.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. registry.addResourceHandler("/recursos/**"):
        // Define el "Alias" o URL virtual pública. Cuando el navegador pida algo en "/recursos/foto.jpg"...
        
        // 2. .addResourceLocations("file:uploads/"):
        // ...Spring buscará físicamente ese archivo dentro de la carpeta "uploads" 
        // ubicada en la raíz de tu proyecto en el disco duro.
        registry.addResourceHandler("/recursos/**")
                .addResourceLocations("file:uploads/");
    }
}