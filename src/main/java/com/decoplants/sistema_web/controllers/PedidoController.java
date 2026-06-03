package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/registrar-pedido")
    public String registrarPedido(@ModelAttribute Pedido pedido, 
                                  @RequestParam Integer idProducto, 
                                  @RequestParam Integer cantidad) {
        
        // Pasamos el pedido (cliente y envío) junto con el producto y cantidad al servicio
        String resultado = pedidoService.procesarNuevoPedido(pedido, idProducto, cantidad);

        if ("ERROR_STOCK".equals(resultado)) {
            return "redirect:/?errorStock";
        }

        return "redirect:/?exito";
    }
}