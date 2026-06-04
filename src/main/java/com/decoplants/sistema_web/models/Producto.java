package com.decoplants.sistema_web.models;

// Importaciones de Jakarta Persistence (JPA) para mapear la clase a la base de datos
import jakarta.persistence.*;

// Importaciones de Jakarta Validation para asegurar que los datos ingresados por el usuario sean correctos
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Importaciones de Lombok para reducir el código repetitivo
import lombok.Data;
import lombok.ToString;

// Importación de la clase BigDecimal de Java: Esencial para cálculos financieros sin errores de redondeo.
import java.math.BigDecimal; 

// Importación de la anotación de Jackson para controlar la serialización JSON y evitar bucles infinitos
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Modelo de la entidad Producto.
 * Representa los artículos físicos (plantas, macetas, etc.) que se ofrecen en el e-commerce.
 * Mapea la tabla "PRODUCTO" en la base de datos SQL Server.
 */
@Data
@Entity
@Table(name = "PRODUCTO")
public class Producto {

    // ==========================================
    // LLAVE PRIMARIA
    // ==========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    // ==========================================
    // ATRIBUTOS Y VALIDACIONES DE NEGOCIO
    // ==========================================

    // @NotBlank verifica que el texto no sea nulo, no esté vacío y no sean solo espacios en blanco.
    // El 'message' es lo que leerá el BindingResult en el controlador si el usuario comete un error.
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    // @Column define la estructura física en SQL: No permite nulos y limita a 150 caracteres.
    @Column(nullable = false, length = 150)
    private String nombre;

    // Permite descripciones largas sin límite predefinido (VARCHAR(MAX))
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    // Cambiamos de Double a BigDecimal para evitar errores de redondeo en coma flotante.
    // También le indicamos a JPA la precisión física en SQL (10 dígitos enteros y 2 decimales)
    @NotNull(message = "Debe ingresar un precio")
    // @Min garantiza a nivel de software que nadie registre un producto con precio negativo.
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2) 
    private BigDecimal precio;

    @NotNull(message = "Debe ingresar el stock inicial")
    // Al igual que el precio, el inventario no puede existir en números negativos.
    @Min(value = 0, message = "El stock no puede ser menor a 0")
    @Column(nullable = false)
    private Integer stock;

    // Almacena la ruta, URL o nombre del archivo de la foto del producto.
    @Column(length = 255)
    private String imagen;

    // Se establece "true" por defecto. Implementa la estrategia de Borrado Lógico (Soft Delete).
    // Si un producto se descontinúa, el estado pasa a "false" en lugar de hacer un DELETE en SQL.
    @Column(nullable = false)
    private Boolean estado = true;

    // ==========================================
    // RELACIONES DE BASE DE DATOS
    // ==========================================
    
    @NotNull(message = "Debe seleccionar una categoría")
    // Relación Many-To-One: Muchos productos pertenecen a una misma categoría.
    // Esta entidad es la "dueña" de la relación porque contiene la llave foránea física en la tabla.
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    @ToString.Exclude // Evita errores de memoria por recursividad infinita al imprimir el objeto con Lombok
    // SOLUCIÓN AL BUCLE INFINITO: Detiene la serialización recursiva de la lista de productos dentro de la categoría
    @JsonIgnoreProperties("productos") 
    private Categoria categoria;
}