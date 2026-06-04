package com.decoplants.sistema_web.models;

// Importaciones de Jakarta Persistence API (JPA) para el mapeo a base de datos
import jakarta.persistence.*;

// Importaciones de la librería Lombok para reducir el código repetitivo (boilerplate)
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Modelo de la entidad Categoría.
 * Representa la estructura de los datos que se guardarán en la base de datos.
 */
// @Data es de Lombok: Genera automáticamente por detrás todos los Getters, Setters, 
// constructores vacíos y el método toString() al momento de compilar.
@Data 
// @Entity le dice a Spring y a Hibernate que esta clase de Java es una tabla en la base de datos.
@Entity 
// @Table especifica explícitamente el nombre exacto de la tabla en el motor de base de datos.
@Table(name = "CATEGORIA") 
public class Categoria {

    // ==========================================
    // DEFINICIÓN DE COLUMNAS (ATRIBUTOS)
    // ==========================================

    // @Id indica que este campo es la Llave Primaria (Primary Key) de la tabla.
    @Id 
    // @GeneratedValue(strategy = GenerationType.IDENTITY) configura la columna como Autoincremental
    // en la base de datos (1, 2, 3, etc.).
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    // @Column mapea el atributo de Java ("idCategoria") al nombre físico de la columna en la BD ("id_categoria").
    @Column(name = "id_categoria") 
    private Integer idCategoria;

    // nullable = false significa que en la BD será un campo NOT NULL. length = 100 limita el tamaño a VARCHAR(100).
    @Column(nullable = false, length = 100) 
    private String nombre;

    @Column(length = 255) 
    private String descripcion;

    // Se establece "true" por defecto para implementar el borrado lógico (Soft Delete).
    @Column(nullable = false) 
    private Boolean estado = true;

    // ==========================================
    // RELACIONES DE BASE DE DATOS
    // ==========================================

    // Relación bidireccional: Una categoría tiene muchos (One-To-Many) productos.
    // mappedBy = "categoria" indica que el atributo "categoria" en la clase Producto es el "dueño" 
    // de la relación (el que lleva la llave foránea).
    @OneToMany(mappedBy = "categoria") 
    // @ToString.Exclude es vital. Cuando Lombok genera el método toString(), intentará imprimir la categoría, 
    // luego sus productos, luego esos productos llamarán de nuevo a la categoría, creando un bucle 
    // infinito que colapsará la memoria (Stack Overflow). Esta anotación lo previene.
    @ToString.Exclude 
    private List<Producto> productos;
}