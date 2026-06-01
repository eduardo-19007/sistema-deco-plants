package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.PedidoRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping("/registrar-pedido")
    public String registrarPedido(@ModelAttribute Pedido pedido) {
        
        // 1. Buscar el producto seleccionado
        Producto producto = productoRepository.findById(pedido.getIdProducto()).orElse(null);
        
        // Validar si existe y si hay stock
        if (producto == null || producto.getStock() < pedido.getCantidad()) {
            return "redirect:/?errorStock"; 
        }

        // 2. CALCULAR EL TOTAL (Precio Unitario * Cantidad)
        BigDecimal cantidadBd = new BigDecimal(pedido.getCantidad());
        BigDecimal totalCalculado = producto.getPrecio().multiply(cantidadBd);
        pedido.setTotal(totalCalculado);

        // 3. DESCONTAR EL STOCK (Gestión de Inventario)
        producto.setStock(producto.getStock() - pedido.getCantidad());
        productoRepository.save(producto);

        // 4. GUARDAR EL PEDIDO
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("Pendiente"); 
        pedidoRepository.save(pedido);

        return "redirect:/?exito";
    }
}