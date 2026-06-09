package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Incidencia;
import com.decoplants.sistema_web.repositories.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


//Controlador para el panel administrativo de Incidencias (Atención al Cliente).
@Controller
@RequestMapping("/admin/incidencias")
public class AdminIncidenciaController {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    // 1. LISTAR INCIDENCIAS (Lectura)
    @GetMapping
    public String listarIncidencias(Model model) {
        model.addAttribute("incidencias", incidenciaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaRegistro")));
        return "admin-incidencias"; 
    }

    // 2. ACTUALIZAR ESTADO (Flujo de Trabajo)
    @PostMapping("/actualizar-estado/{id}")
    public String actualizarEstado(
            @PathVariable("id") Integer id, 
            @RequestParam("nuevoEstado") String nuevoEstado) {
        Incidencia incidencia = incidenciaRepository.findById(id).orElse(null);
        if (incidencia != null) {
            incidencia.setEstado(nuevoEstado);
            incidenciaRepository.save(incidencia);
        }
        return "redirect:/admin/incidencias";
    }
}