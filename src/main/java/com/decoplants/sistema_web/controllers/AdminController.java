package com.decoplants.sistema_web.controllers;

// Importación de los modelos y repositorios propios del proyecto
import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.repositories.PedidoRepository;

// Importaciones del framework Spring Boot necesarias para la web y dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Importación para manejar colecciones de datos
import java.util.List;

/**
 * Controlador para la gestión de pedidos en el panel de administración.
 * Cumple el rol de "Controlador" dentro del patrón de arquitectura MVC, 
 * procesando las peticiones del usuario, interactuando con la base de datos 
 * y devolviendo la vista correspondiente.
 */
@Controller
public class AdminController {

    // @Autowired realiza la inyección de dependencias. 
    // Spring crea y asigna automáticamente la instancia de PedidoRepository 
    // sin que tengamos que hacer un "new PedidoRepository()".
    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Muestra la interfaz con la lista de todos los pedidos.
     * @GetMapping indica que este método responde a peticiones HTTP GET (cuando el usuario entra a la URL).
     */
    @GetMapping("/admin/pedidos")
    public String verPedidos(Model model) {
        // 1. Consulta a la base de datos y recupera todos los registros de pedidos.
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        
        // 2. El objeto 'Model' transfiere datos del controlador a la vista.
        // Aquí pasamos la lista recuperada bajo el nombre "pedidos" para que la plantilla HTML la pueda leer.
        model.addAttribute("pedidos", listaPedidos);
        
        // 3. Retorna el nombre del archivo de la vista (ej. admin-pedidos.html) que se mostrará al administrador.
        return "admin-pedidos";
    }

    /**
     * Actualiza el estado de un pedido específico.
     * @PostMapping indica que responde a envíos de formularios (HTTP POST).
     * El "{id}" en la ruta es dinámico y representa el número del pedido a modificar.
     */
    // PROCESO 3 y 4: Actualización progresiva de estados
    @PostMapping("/admin/pedidos/actualizar-estado/{id}")
    public String actualizarEstado(
            @PathVariable Integer id,       // Extrae el ID directamente de la URL.
            @RequestParam String nuevoEstado // Captura el valor del campo "nuevoEstado" enviado en el formulario.
    ) {
        // 1. Busca el pedido por su ID. Como findById devuelve un 'Optional', 
        // usamos .orElse(null) para que asigne nulo si el pedido no existe en la base de datos.
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        
        // 2. Validación de seguridad: Solo procedemos si el pedido fue encontrado.
        if (pedido != null) {
            // Flujo lógico esperado: Pendiente -> En Preparación -> En Ruta -> Entregado
            
            // 3. Se actualiza el atributo de estado en el objeto Java.
            pedido.setEstado(nuevoEstado); 
            
            // 4. Se guarda el objeto modificado, ejecutando un UPDATE en la base de datos subyacente.
            pedidoRepository.save(pedido); 
        }
        
        // 5. Redirección: En lugar de cargar una vista directamente, enviamos al usuario 
        // de vuelta a la ruta "/admin/pedidos". Esto evita que recargar la página duplique el envío del formulario.
        return "redirect:/admin/pedidos";
    }
}