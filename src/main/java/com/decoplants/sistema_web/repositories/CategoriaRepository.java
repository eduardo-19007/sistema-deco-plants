package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Con esto ya tenemos acceso a .findAll() y .findById() para categorías
}