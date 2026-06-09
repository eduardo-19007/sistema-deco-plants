package com.decoplants.sistema_web.controllers;
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


 //Controlador principal (Home).
@Controller
public class HomeController {

    // Inyección de dependencias. Spring inicializa ProductoRepository automáticamente
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        List<Producto> catalogo = productoRepository.findByEstadoTrue();
        
        model.addAttribute("productos", catalogo);
        return "index";
    }
}