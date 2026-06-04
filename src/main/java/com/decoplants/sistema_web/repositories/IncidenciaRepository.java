package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    // Interfaz vacía. Al extender de JpaRepository, es suficiente para 
    // realizar el CRUD básico completo de las incidencias (reclamos).
}