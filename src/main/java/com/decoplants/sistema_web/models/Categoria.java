package com.decoplants.sistema_web.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@Entity
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

    // Relación bidireccional: Una categoría tiene muchos productos
    // El @ToString.Exclude evita el error de bucle infinito (Stack Overflow)
    @OneToMany(mappedBy = "categoria")
    @ToString.Exclude
    private List<Producto> productos;
}