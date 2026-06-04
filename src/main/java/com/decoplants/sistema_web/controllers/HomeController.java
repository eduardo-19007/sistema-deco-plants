package com.decoplants.sistema_web.controllers;

// Importaciones del modelo (la entidad) y el repositorio (acceso a base de datos)
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.ProductoRepository;

// Anotaciones y clases core de Spring Boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Manejo de listas en Java
import java.util.List;

/**
 * Controlador principal (Home).
 * Su responsabilidad única es gestionar la vista inicial o "página de aterrizaje" (Landing Page) 
 * que ven los clientes al entrar al sistema web de Decoplants.
 */
@Controller
public class HomeController {

    // Inyección de dependencias. Spring inicializa ProductoRepository automáticamente
    // para que podamos interactuar con la tabla de productos en la base de datos.
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Gestiona la ruta raíz o principal del sitio web.
     * @GetMapping("/") indica que este método se ejecutará cuando un usuario 
     * ingrese a la URL principal (por ejemplo, www.tudominio.com o localhost:8080/).
     */
    @GetMapping("/")
    public String inicio(Model model) {
        // 1. Consulta optimizada a la base de datos: 
        // Llama a un método personalizado del repositorio que busca estrictamente 
        // los productos cuyo atributo 'estado' sea verdadero (true).
        // Esto evita cargar productos ocultos, agotados o descontinuados.
        List<Producto> catalogo = productoRepository.findByEstadoTrue();
        
        // 2. Transferencia de datos a la vista:
        // Envía la lista filtrada de productos activos bajo el alias "productos".
        // La plantilla HTML (index) iterará sobre esta lista para mostrar el catálogo al cliente.
        model.addAttribute("productos", catalogo);
        
        // 3. Retorno de la vista:
        // Spring Boot buscará un archivo llamado "index.html" (usualmente en la carpeta templates) 
        // y lo renderizará en el navegador del usuario.
        return "index";
    }
}