package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.CategoriaRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/productos")
public class ProductoWebController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Mostrar el formulario en pantalla
    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        // Le enviamos un producto vacío al formulario para enlazar los campos
        model.addAttribute("producto", new Producto());
        // Le enviamos la lista de categorías para el <select>
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        return "formulario-producto"; // Esto llamará al archivo formulario-producto.html
    }

    // 2. Recibir los datos y VALIDARLOS
    @PostMapping("/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto, BindingResult result, Model model) {
        
        // ¡MAGIA DE SPRING VALIDATOR!
        // Si el usuario puso un precio negativo o dejó el nombre vacío, result.hasErrors() será TRUE.
        if (result.hasErrors()) {
            // Recargamos la lista de categorías para que no se borren del formulario
            model.addAttribute("categorias", categoriaRepository.findAll());
            
            // Devolvemos al usuario a la misma página para que vea las letras rojas de error
            return "formulario-producto"; 
        }

        // Si pasó las validaciones, lo guardamos en SQL Server
        productoRepository.save(producto);
        
        // Redirigimos al panel de pedidos (por ahora) mostrando un mensaje de éxito
        return "redirect:/admin/pedidos?exitoProducto";
    }

    // 3. Mostrar la lista de todos los productos
    @GetMapping("/lista")
    public String listarProductos(Model model) {
        // Obtenemos todos los productos de la base de datos
        // y los mandamos a la vista bajo el nombre "productos"
        model.addAttribute("productos", productoRepository.findAll());
        
        return "admin-productos"; // Llamará al archivo admin-productos.html
    }

    // 4. Cargar los datos de un producto en el formulario para EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable("id") Integer id, Model model) {
        // Buscamos el producto por su ID. Si no existe, devuelve null
        Producto producto = productoRepository.findById(id).orElse(null);
        
        // Enviamos el producto encontrado y la lista de categorías al formulario
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // ¡Reutilizamos la misma vista del formulario!
        return "formulario-producto"; 
    }

    // 5. ELIMINAR un producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        // Borramos el producto de SQL Server
        productoRepository.deleteById(id);
        
        // Redirigimos de vuelta a la tabla
        return "redirect:/admin/productos/lista";
    }
}