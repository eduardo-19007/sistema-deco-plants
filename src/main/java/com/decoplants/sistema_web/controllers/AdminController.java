package com.decoplants.sistema_web.controllers;
import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


 //Controlador para la gestión de pedidos en el panel de administración.

@Controller
public class AdminController {

    // @Autowired realiza la inyección de dependencias. 
    @Autowired
    private PedidoRepository pedidoRepository;


    //Muestra la interfaz con la lista de todos los pedidos.
    @GetMapping("/admin/pedidos")
    public String verPedidos(Model model) {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        model.addAttribute("pedidos", listaPedidos);
        return "admin-pedidos";
    }


    // Actualiza el estado de un pedido específico.
    @PostMapping("/admin/pedidos/actualizar-estado/{id}")
    public String actualizarEstado(
            @PathVariable Integer id,   
            @RequestParam String nuevoEstado 
    ) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido != null) {
            pedido.setEstado(nuevoEstado); 
            pedidoRepository.save(pedido); 
        }
        return "redirect:/admin/pedidos";
    }
}