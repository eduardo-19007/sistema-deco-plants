package com.decoplants.sistema_web.controllers;

// Importaciones básicas del framework Spring para el manejo de rutas web
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador dedicado a la gestión del inicio de sesión (Login).
 * Su única responsabilidad en este punto es interceptar la petición a la ruta "/login" 
 * y devolver la vista (interfaz gráfica) correspondiente al usuario.
 */
@Controller
public class LoginController {

    /**
     * Muestra el formulario de autenticación.
     * @GetMapping("/login") captura cualquier petición HTTP GET hacia la ruta "/login".
     * A diferencia de otros controladores, no requiere inyectar modelos ni repositorios 
     * porque solo necesita cargar un archivo estático o plantilla.
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        // Retorna el String "login". 
        // El motor de plantillas de Spring (como Thymeleaf) buscará automáticamente 
        // un archivo llamado "login.html" en el directorio "src/main/resources/templates/" 
        // y lo enviará al navegador web.
        return "login"; 
    }
}