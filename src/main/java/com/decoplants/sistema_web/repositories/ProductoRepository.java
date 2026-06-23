package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Método original (lo mantenemos por si se usa en otro lado)
    List<Producto> findByEstadoTrue();

    // NUEVO MÉTODO CON PAGINACIÓN: Devuelve solo los activos, paginados
    Page<Producto> findByEstadoTrue(Pageable pageable);
}