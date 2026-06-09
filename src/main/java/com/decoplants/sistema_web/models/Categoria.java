package com.decoplants.sistema_web.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

// Genera automáticamente por detrás todos los Getters, Setters, 
@Data 
// @Entity le dice a Spring y a Hibernate que esta clase de Java es una tabla en la base de datos.
@Entity 
// @Table especifica explícitamente el nombre exacto de la tabla en el motor de base de datos.
@Table(name = "CATEGORIA") 
public class Categoria {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id_categoria") 
    private Integer idCategoria;

    @Column(nullable = false, length = 100) 
    private String nombre;

    @Column(length = 255) 
    private String descripcion;

    @Column(nullable = false) 
    private Boolean estado = true;

    // Relación bidireccional: Una categoría tiene muchos (One-To-Many) productos.
    @OneToMany(mappedBy = "categoria") 
    // @ToString.Exclude es vital. Cuando Lombok genera el método toString(), intentará imprimir la categoría, 
    @ToString.Exclude 
    private List<Producto> productos;
}