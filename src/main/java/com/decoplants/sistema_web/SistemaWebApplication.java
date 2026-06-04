package com.decoplants.sistema_web;

// Importaciones del núcleo de Spring Boot
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;   

/**
 * Clase principal (Main) del sistema.
 * Es el punto de entrada de la aplicación Java.
 */
// =========================================================
// LA ANOTACIÓN MÁS IMPORTANTE DEL PROYECTO
// =========================================================
// @SpringBootApplication es en realidad una "súper anotación" que combina tres cosas:
// 1. @Configuration: Permite registrar beans o configuraciones extra (como tu SecurityConfig).
// 2. @EnableAutoConfiguration: Le dice a Spring que configure automáticamente la base de datos, 
//    el servidor web y la seguridad basándose en las dependencias instaladas.
// 3. @ComponentScan: Escanea automáticamente este paquete (com.decoplants.sistema_web) y todas 
//    sus subcarpetas buscando las etiquetas @Controller, @Service, y @Repository para inicializarlas.
@SpringBootApplication 
public class SistemaWebApplication {
    
    /**
     * El método public static void main es el estándar de cualquier programa Java.
     * Es lo primero que ejecuta la Máquina Virtual de Java (JVM) al arrancar.
     */
    public static void main(String[] args) {
        // SpringApplication.run() realiza el levantamiento pesado:
        // - Arranca el framework de Spring.
        // - Inicia el servidor web embebido (Tomcat) en el puerto 8080 (por defecto).
        // - Despliega nuestra aplicación web para que empiece a escuchar peticiones HTTP.
        SpringApplication.run(SistemaWebApplication.class, args);
    }
}