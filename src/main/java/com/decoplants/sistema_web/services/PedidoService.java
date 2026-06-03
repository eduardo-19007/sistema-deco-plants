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

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public String procesarNuevoPedido(Pedido pedido, Integer idProducto, Integer cantidad) {
        
        // 1. Buscamos el producto
        Producto producto = productoRepository.findById(idProducto).orElse(null);

        // 2. Validamos existencia y stock
        if (producto == null || producto.getStock() < cantidad) {
            return "ERROR_STOCK";
        }

        // =========================================================
        // 3. ARMADO DEL DETALLE DEL PEDIDO (El "Cuerpo")
        // =========================================================
        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio()); // Guardamos el precio actual

        // Calculamos el subtotal (precio * cantidad)
        BigDecimal cantidadDecimal = new BigDecimal(cantidad);
        BigDecimal subtotal = producto.getPrecio().multiply(cantidadDecimal);
        detalle.setSubtotal(subtotal);

        // =========================================================
        // 4. ARMADO DEL PEDIDO FINAL (La "Cabecera")
        // =========================================================
        // Vinculamos el detalle al pedido (nuestro método de ayuda en Pedido.java)
        pedido.addDetalle(detalle);
        
        // Como es una compra rápida de 1 solo tipo de producto, el total del pedido es igual al subtotal
        pedido.setTotalPedido(subtotal); 
        
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("Pendiente"); 

        // =========================================================
        // 5. ACTUALIZAR BASE DE DATOS
        // =========================================================
        // Descontamos stock del inventario
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        // Al guardar el Pedido, CascadeType.ALL guarda el DetallePedido automáticamente
        pedidoRepository.save(pedido);

        return "EXITO";
    }
}