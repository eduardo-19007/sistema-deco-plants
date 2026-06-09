package com.decoplants.sistema_web.services;
import com.decoplants.sistema_web.models.DetallePedido;
import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.PedidoRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;


//Capa de Servicios para la gestión de Pedidos.
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public String procesarNuevoPedido(Pedido pedido, Integer idProducto, Integer cantidad) {

        Producto producto = productoRepository.findById(idProducto).orElse(null);

        if (producto == null || producto.getStock() < cantidad) {
            return "ERROR_STOCK"; 
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);

        detalle.setPrecioUnitario(producto.getPrecio()); 

        BigDecimal cantidadDecimal = new BigDecimal(cantidad);
        BigDecimal subtotal = producto.getPrecio().multiply(cantidadDecimal);
        detalle.setSubtotal(subtotal);

        pedido.addDetalle(detalle);

        pedido.setTotalPedido(subtotal); 

        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("Pendiente"); 

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto); // UPDATE en la tabla PRODUCTO

        pedidoRepository.save(pedido);

        return "EXITO";
    }
}