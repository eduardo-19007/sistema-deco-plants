package com.decoplants.sistema_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @NotNull(message = "Debe ingresar un precio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @NotNull(message = "Debe ingresar el stock inicial")
    @Min(value = 0, message = "El stock no puede ser menor a 0")
    @Column(nullable = false)
    private Integer stock;

    @Column(length = 255)
    private String imagen;

    @Column(nullable = false)
    private Boolean estado = true;

    // Relación: Muchos productos pertenecen a una categoría
    // El @ToString.Exclude es obligatorio aquí para romper el ciclo de lectura
    @NotNull(message = "Debe seleccionar una categoría")
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @ToString.Exclude
    private Categoria categoria;
}