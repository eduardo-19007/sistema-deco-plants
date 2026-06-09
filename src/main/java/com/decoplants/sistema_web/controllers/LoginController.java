package com.decoplants.sistema_web.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//Controlador dedicado a la gestión del inicio de sesión (Login).

@Controller
public class LoginController {

    //@GetMapping("/login") captura cualquier petición HTTP GET hacia la ruta "/login".

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; 
    }
}