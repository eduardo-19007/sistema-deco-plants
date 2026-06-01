package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Pedido;
import com.decoplants.sistema_web.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/admin/pedidos")
    public String verPedidos(Model model) {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        model.addAttribute("pedidos", listaPedidos);
        return "admin-pedidos";
    }

    // PROCESO 3 y 4: Actualización progresiva de estados
    @PostMapping("/admin/pedidos/actualizar-estado/{id}")
    public String actualizarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        
        if (pedido != null) {
            // Flujo: Pendiente -> En Preparación -> En Ruta -> Entregado
            pedido.setEstado(nuevoEstado); 
            pedidoRepository.save(pedido); 
        }
        return "redirect:/admin/pedidos";
    }
}