package com.decoplants.sistema_web.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
    @GetMapping("/usuario")
    public String panelUsuario() {
        return "usuario"; // Devuelve la vista requerida por el docente
    }
}