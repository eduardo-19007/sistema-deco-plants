package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Incidencia;
import com.decoplants.sistema_web.repositories.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.time.LocalDateTime;

@Controller
public class PostventaController {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @PostMapping("/registrar-incidencia")
    public String registrarIncidencia(@ModelAttribute Incidencia incidencia) {
        incidencia.setFechaRegistro(LocalDateTime.now());
        incidencia.setEstado("Abierto");
        incidenciaRepository.save(incidencia);
        return "redirect:/?reclamoExito";
    }
}