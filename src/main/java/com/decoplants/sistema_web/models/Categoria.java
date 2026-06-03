package com.decoplants.sistema_web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CATEGORIA")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria; // Lo cambié a Integer para mantener estándar con las otras tablas

    // --- VALIDACIONES Y RESTRICCIONES ---
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @Column(nullable = false)
    private Boolean estado = true;

    // --- RELACIÓN ORM (1 a N) ---
    // Una categoría tiene muchos productos. 
    // CascadeType.ALL significa que si eliminas una categoría, podrías afectar sus productos.
    // @JsonIgnore es vital para las APIs REST: Evita un bucle infinito al convertir a JSON.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Producto> productos;
}