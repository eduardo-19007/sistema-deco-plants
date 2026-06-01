package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Método personalizado para traer solo los productos activos (estado = true)
    List<Producto> findByEstadoTrue();
}