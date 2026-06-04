package com.decoplants.sistema_web.repositories;

import com.decoplants.sistema_web.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Repository le indica al framework que esta interfaz es un componente de acceso a datos.
 * Spring interceptará los errores de base de datos nativos (ej. de SQL Server) 
 * y los traducirá a excepciones estándar de Java.
 */
@Repository
// JpaRepository recibe dos parámetros genéricos: <La Entidad, El tipo de dato de su Llave Primaria>
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Al heredar de JpaRepository, automáticamente heredamos docenas de métodos pre-programados
    // Con esto ya tenemos acceso a .findAll(), .findById(), .deleteById(), etc., sin escribir SQL.
}