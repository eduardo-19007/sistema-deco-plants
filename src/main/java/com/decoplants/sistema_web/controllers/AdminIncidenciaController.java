package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Incidencia;
import com.decoplants.sistema_web.repositories.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el panel administrativo de Incidencias (Atención al Cliente).
 * Protegido por Spring Security bajo la ruta "/admin/**"
 */
@Controller
@RequestMapping("/admin/incidencias")
public class AdminIncidenciaController {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    // ==========================================
    // 1. LISTAR INCIDENCIAS (Lectura)
    // ==========================================
    @GetMapping
    public String listarIncidencias(Model model) {
        // Utilizamos Sort.by para ordenarlas de forma descendente (DESC) por la fecha de registro.
        // Así, el administrador siempre verá los reclamos más urgentes o recientes en la parte superior.
        model.addAttribute("incidencias", incidenciaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro")));
        
        // Retorna el nombre de la plantilla HTML que crearemos a continuación
        return "admin-incidencias"; 
    }

    // ==========================================
    // 2. ACTUALIZAR ESTADO (Flujo de Trabajo)
    // ==========================================
    @PostMapping("/actualizar-estado/{id}")
    public String actualizarEstado(
            @PathVariable("id") Integer id, 
            @RequestParam("nuevoEstado") String nuevoEstado) {
        
        // Buscamos el ticket original en la base de datos
        Incidencia incidencia = incidenciaRepository.findById(id).orElse(null);
        
        // Si existe, le actualizamos el estado y hacemos un UPDATE en SQL Server
        if (incidencia != null) {
            incidencia.setEstado(nuevoEstado);
            incidenciaRepository.save(incidencia);
        }
        
        // Patrón Post-Redirect-Get: Redirigimos a la lista para evitar reenvíos de formulario
        return "redirect:/admin/incidencias";
    }
}