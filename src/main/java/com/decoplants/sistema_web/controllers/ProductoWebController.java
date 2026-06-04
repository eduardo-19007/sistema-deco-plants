package com.decoplants.sistema_web.controllers;

// Importaciones de modelos y repositorios
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.CategoriaRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;

// Importaciones para validación de formularios
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

// Importaciones de Spring MVC
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Controlador Web para la gestión de Productos desde el panel de administración.
 * A diferencia del ProductoRestController (que devolvía JSON), este controlador 
 * interactúa directamente con las plantillas HTML (Thymeleaf/JSP).
 */
@Controller
// Todas las rutas de este archivo comenzarán con "/admin/productos"
@RequestMapping("/admin/productos")
public class ProductoWebController {

    // Inyección de dependencias para interactuar con las tablas de Productos y Categorías
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ==========================================
    // 1. MOSTRAR FORMULARIO DE REGISTRO (GET)
    // ==========================================
    @GetMapping("/nuevo")
    public String mostrarFormularioDeRegistro(Model model) {
        // 1. Objeto de respaldo (Backing Object):
        // Enviamos un objeto Producto vacío a la vista. Esto permite que el formulario HTML 
        // "enlace" sus inputs (th:field) directamente a los atributos de esta clase vacía.
        model.addAttribute("producto", new Producto());
        
        // 2. Diccionario de datos:
        // Cargamos todas las categorías de la base de datos para llenar la etiqueta <select> del formulario.
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        return "formulario-producto"; // Renderiza la vista "formulario-producto.html"
    }

    // ==========================================
    // 2. GUARDAR DATOS Y VALIDAR (POST)
    // ==========================================
    @PostMapping("/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto, 
            BindingResult result, 
            Model model,
            // Agregamos @RequestParam para capturar el archivo que viene del formulario
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "formulario-producto"; 
        }

        // ==========================================
        // LÓGICA DE SUBIDA DE IMAGEN
        // ==========================================
        if (file != null && !file.isEmpty()) {
            try {
                // 1. Generar un nombre único para evitar que fotos con el mismo nombre se chanquen
                // Ej: "flor.jpg" se convierte en "f83b2-flor.jpg"
                String nombreArchivo = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                
                // 2. Definir la carpeta donde se guardarán (se creará una carpeta "uploads" en la raíz de tu proyecto)
                Path directorioImagenes = Paths.get("uploads");
                if (!Files.exists(directorioImagenes)) {
                    Files.createDirectories(directorioImagenes);
                }
                
                // 3. Ruta completa del archivo
                Path rutaAbsoluta = directorioImagenes.resolve(nombreArchivo);
                
                // 4. Copiar el archivo físico a la carpeta
                Files.copy(file.getInputStream(), rutaAbsoluta);
                
                // 5. Guardar SOLO el nombre del archivo en el objeto Producto para SQL Server
                producto.setImagen(nombreArchivo);
                
            } catch (Exception e) {
                e.printStackTrace();
                // Aquí podrías agregar un mensaje de error al Model si falla la subida
            }
        } else if (producto.getIdProducto() != null) {
            // LÓGICA DE EDICIÓN: Si estamos editando y no subimos foto nueva, 
            // recuperamos la foto antigua de la base de datos para no borrarla.
            Producto pExistente = productoRepository.findById(producto.getIdProducto()).orElse(null);
            if(pExistente != null){
                producto.setImagen(pExistente.getImagen());
            }
        }

        productoRepository.save(producto);
        return "redirect:/admin/productos/lista";
    }

    // ==========================================
    // 3. LISTAR PRODUCTOS (GET)
    // ==========================================
    @GetMapping("/lista")
    public String listarProductos(Model model) {
        // Ejecuta un SELECT * FROM productos y manda la lista a la vista "admin-productos.html"
        model.addAttribute("productos", productoRepository.findAll());
        return "admin-productos"; 
    }

    // ==========================================
    // 4. MOSTRAR FORMULARIO PARA EDITAR (GET)
    // ==========================================
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable("id") Integer id, Model model) {
        // 1. Búsqueda del registro existente
        Producto producto = productoRepository.findById(id).orElse(null);
        
        // 2. Carga de datos al modelo
        // En lugar de enviar un 'new Producto()' vacío (como en el método "nuevo"), 
        // enviamos el producto con sus datos reales para que los inputs del formulario aparezcan llenos.
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        // 3. Reutilización de código:
        // Volvemos a llamar a "formulario-producto". El mismo archivo HTML sirve tanto para crear como para editar.
        return "formulario-producto"; 
    }

    // ==========================================
    // 5. ELIMINAR PRODUCTO (GET)
    // ==========================================
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        // Ejecuta un DELETE en SQL Server basándose en la llave primaria (ID)
        productoRepository.deleteById(id);
        
        // Redirige a la tabla de productos para que se vea reflejado el cambio (el producto ya no está)
        return "redirect:/admin/productos/lista";
    }
}