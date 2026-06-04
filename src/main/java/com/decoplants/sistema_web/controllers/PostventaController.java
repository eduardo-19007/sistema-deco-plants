package com.decoplants.sistema_web.controllers;

// Importaciones de la entidad Incidencia y su repositorio
import com.decoplants.sistema_web.models.Incidencia;
import com.decoplants.sistema_web.repositories.IncidenciaRepository;

// Importaciones de Spring Boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

// Importación para el manejo de fechas y horas exactas
import java.time.LocalDateTime;

/**
 * Controlador encargado de la gestión de incidencias, quejas o reclamos (Postventa).
 * Permite a los clientes registrar problemas con sus pedidos desde la interfaz web.
 */
@Controller
public class PostventaController {

    // Inyectamos el repositorio para poder guardar las incidencias directamente en la base de datos.
    @Autowired
    private IncidenciaRepository incidenciaRepository;

    /**
     * Intercepta el formulario de registro de una nueva incidencia.
     * @PostMapping indica que este método solo se ejecuta cuando se envían datos mediante HTTP POST.
     */
    @PostMapping("/registrar-incidencia")
    public String registrarIncidencia(
            // @ModelAttribute mapea los datos del formulario (ej. título, descripción, correo)
            // y construye automáticamente el objeto 'Incidencia'.
            @ModelAttribute Incidencia incidencia
    ) {
        
        // 1. Enriquecimiento de datos del lado del servidor:
        // El cliente solo envía el texto de su queja, pero el servidor debe completar 
        // los metadatos críticos por razones de auditoría y seguridad.
        
        // Asignamos la fecha y hora EXACTA del servidor en el momento en que se procesa la petición.
        incidencia.setFechaRegistro(LocalDateTime.now());
        
        // Todo reclamo nuevo entra por defecto en estado "Abierto" para que un administrador lo revise.
        incidencia.setEstado("Abierto");
        
        // 2. Persistencia:
        // Guardamos el objeto completo (datos del cliente + fecha + estado) en la base de datos.
        incidenciaRepository.save(incidencia);
        
        // 3. Redirección y Feedback al usuario:
        // Usamos el patrón Post-Redirect-Get. Volvemos a la página principal agregando la bandera "?reclamoExito"
        // para que el frontend (HTML/JS) detecte este parámetro y muestre un mensaje de confirmación al cliente.
        return "redirect:/?reclamoExito";
    }
}