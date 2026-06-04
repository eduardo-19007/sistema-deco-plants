package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    /**
     * Consulta Derivada (Derived Query).
     * Spring analiza el nombre del método ("findBy" + "Estado" + "True") y 
     * automáticamente construye y ejecuta la consulta SQL equivalente en tiempo de ejecución:
     * SELECT * FROM PRODUCTO WHERE estado = 1;
     * * Método personalizado para traer solo los productos activos (catálogo público).
     */
    List<Producto> findByEstadoTrue();
}