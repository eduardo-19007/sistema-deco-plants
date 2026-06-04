package com.decoplants.sistema_web.controllers;

// Importaciones de los modelos y servicios
import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.services.PedidoService;

// Importaciones de Spring Boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador encargado de gestionar la creación de nuevos pedidos desde la vista del cliente.
 * Intercepta los datos del formulario de compra y los envía a la capa lógica de negocio.
 */
@Controller
public class PedidoController {

    // A diferencia de los controladores anteriores que llamaban directamente al Repository,
    // aquí inyectamos un Service. Esto se hace porque registrar un pedido implica
    // "lógica de negocio" compleja (como revisar el stock), que no debe ir en el controlador.
    @Autowired
    private PedidoService pedidoService;

    /**
     * Procesa el formulario cuando un cliente finaliza una compra.
     * @PostMapping("/registrar-pedido") intercepta el envío (submit) del formulario de compra.
     */
    @PostMapping("/registrar-pedido")
    public String registrarPedido(
            // @ModelAttribute mapea automáticamente los campos del formulario HTML (nombre, dirección, etc.)
            // directamente a las propiedades del objeto 'Pedido', ahorrándonos hacerlo manualmente.
            @ModelAttribute Pedido pedido, 
            
            // @RequestParam captura valores individuales específicos que se enviaron en el formulario,
            // en este caso, qué producto se quiere comprar y cuántas unidades.
            @RequestParam Integer idProducto, 
            @RequestParam Integer cantidad
    ) {
        
        // 1. Delegación de la lógica de negocio:
        // El controlador no hace cálculos ni consultas directas. Le pasa todos los datos
        // recolectados a la capa de Servicio para que esta haga el trabajo pesado (validar stock, guardar, etc.).
        // El servicio nos devuelve un String indicando cómo terminó el proceso.
        String resultado = pedidoService.procesarNuevoPedido(pedido, idProducto, cantidad);

        // 2. Manejo de la respuesta (Control de flujo):
        // Verificamos si el servicio detectó que no hay suficiente stock.
        // Nota: Es una buena práctica usar "CONSTANTE".equals(variable) para evitar errores NullPointerException.
        if ("ERROR_STOCK".equals(resultado)) {
            // Si falta stock, redirigimos al Home (/) y le agregamos un parámetro en la URL (?errorStock).
            // La vista frontend puede leer este parámetro para mostrar una alerta (ej. un SweetAlert o un toast rojo).
            return "redirect:/?errorStock";
        }

        // 3. Redirección en caso de éxito:
        // Si todo salió bien, redirigimos al Home con una bandera de éxito (?exito).
        // Nuevamente usamos el patrón Post-Redirect-Get para evitar reenvíos accidentales del formulario.
        return "redirect:/?exito";
    }
}