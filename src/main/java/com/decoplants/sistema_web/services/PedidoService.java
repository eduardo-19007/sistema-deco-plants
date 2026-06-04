package com.decoplants.sistema_web.services;

// Importaciones de Modelos (Entidades)
import com.decoplants.sistema_web.models.DetallePedido;
import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.models.Producto;

// Importaciones de Repositorios (Acceso a BD)
import com.decoplants.sistema_web.repositories.PedidoRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;

// Importaciones de Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Importaciones utilitarias de Java
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Capa de Servicios para la gestión de Pedidos.
 * @Service le indica a Spring que esta clase contiene la "Lógica de Negocio".
 * Aquí es donde se toman decisiones, se hacen cálculos y se aplican las reglas
 * de la empresa antes de tocar la base de datos.
 */
@Service
public class PedidoService {

    // Inyectamos los repositorios necesarios para consultar y actualizar datos
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Procesa la compra de un producto.
     * @Transactional es VITAL. Garantiza las propiedades ACID (Atomicidad, Consistencia, 
     * Aislamiento, Durabilidad). Si ocurre un error en la línea 66, los cambios de la 
     * línea 62 se cancelan automáticamente (Rollback), evitando inconsistencias.
     */
    @Transactional
    public String procesarNuevoPedido(Pedido pedido, Integer idProducto, Integer cantidad) {
        
        // 1. Búsqueda del recurso: Intentamos localizar el producto que el cliente quiere comprar
        Producto producto = productoRepository.findById(idProducto).orElse(null);

        // 2. Validación de Reglas de Negocio (Fail-Fast)
        // Si el producto no existe o el cliente pide más plantas de las que hay en el inventario, abortamos.
        if (producto == null || producto.getStock() < cantidad) {
            return "ERROR_STOCK"; // Este String será interceptado por el PedidoController
        }

        // =========================================================
        // 3. ARMADO DEL DETALLE DEL PEDIDO (El "Cuerpo")
        // =========================================================
        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        
        // Congelamos el precio: Guardamos el precio que tiene el producto HOY.
        // Si mañana el administrador sube el precio de la planta, este pedido histórico no se alterará.
        detalle.setPrecioUnitario(producto.getPrecio()); 

        // Operación matemática con BigDecimal: No se puede usar el operador "*".
        // Convertimos la cantidad (Integer) a BigDecimal para poder multiplicarlos con precisión exacta.
        BigDecimal cantidadDecimal = new BigDecimal(cantidad);
        BigDecimal subtotal = producto.getPrecio().multiply(cantidadDecimal);
        detalle.setSubtotal(subtotal);

        // =========================================================
        // 4. ARMADO DEL PEDIDO FINAL (La "Cabecera")
        // =========================================================
        
        // Sincronización en memoria: Vinculamos bidireccionalmente el detalle con su cabecera
        pedido.addDetalle(detalle);
        
        // Como este flujo específico es para una "Compra Rápida" (Buy Now) de 1 solo ítem, 
        // el total a pagar del pedido es exactamente igual al subtotal del único detalle.
        pedido.setTotalPedido(subtotal); 
        
        // Sellado de tiempo y estado inicial del flujo de trabajo (Workflow)
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("Pendiente"); 

        // =========================================================
        // 5. PERSISTENCIA Y ACTUALIZACIÓN EN BASE DE DATOS
        // =========================================================
        
        // Regla contable: Reducir el inventario físico para que otros clientes no compren lo que ya no hay
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto); // UPDATE en la tabla PRODUCTO

        // Guardado maestro: Gracias al CascadeType.ALL definido en la clase Pedido,
        // esto ejecuta un INSERT en la tabla PEDIDO y automáticamente otro INSERT en DETALLE_PEDIDO.
        pedidoRepository.save(pedido);

        // Retornamos la bandera de éxito al controlador
        return "EXITO";
    }
}