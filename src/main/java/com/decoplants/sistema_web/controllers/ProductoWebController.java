package com.decoplants.sistema_web.controllers;
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.services.CategoriaService;
import com.decoplants.sistema_web.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


//Controlador Web para la gestión de Productos desde el panel de administración.

@Controller
@RequestMapping("/admin/productos")
public class ProductoWebController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // 1. MOSTRAR FORMULARIO DE REGISTRO (GET)
    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "formulario-producto"; 
    }

    // 2. GUARDAR DATOS Y VALIDAR (POST)
    @PostMapping("/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto, 
            BindingResult result, 
            Model model,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "formulario-producto"; 
        }

        // LÓGICA DE SUBIDA DE IMAGEN
        if (file != null && !file.isEmpty()) {
            try {
                String nombreArchivo = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                Path directorioImagenes = Paths.get("uploads");
                if (!Files.exists(directorioImagenes)) {
                    Files.createDirectories(directorioImagenes);
                }
                Path rutaAbsoluta = directorioImagenes.resolve(nombreArchivo);
                Files.copy(file.getInputStream(), rutaAbsoluta);
                producto.setImagen(nombreArchivo);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (producto.getIdProducto() != null) {
            Producto pExistente = productoService.obtenerPorId(producto.getIdProducto());
            if(pExistente != null){
                producto.setImagen(pExistente.getImagen());
            }
        }
        productoService.guardar(producto);
        return "redirect:/admin/productos/lista";
    }

    @GetMapping("/lista")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "admin-productos"; 
    }


    // MOSTRAR FORMULARIO PARA EDITAR (GET)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "formulario-producto"; 
    }

    //ELIMINAR PRODUCTO (GET)
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.eliminar(id);
        return "redirect:/admin/productos/lista";
    }
}