package com.decoplants.sistema_web.models;

// Importaciones de Jakarta Persistence API (JPA) para el mapeo objeto-relacional
import jakarta.persistence.*;

// Importaciones de Lombok para la generación automática de código
import lombok.Data;
import lombok.ToString;

// Importación esencial para el manejo de datos monetarios y financieros de alta precisión
import java.math.BigDecimal;

/**
 * Modelo de la entidad DetallePedido.
 * Representa cada una de las líneas o ítems que componen un pedido general.
 * Mapea la tabla "DETALLE_PEDIDO" en la base de datos.
 */
@Entity
@Table(name = "DETALLE_PEDIDO")
@Data // Genera getters, setters, equals, hashCode y toString automáticamente con Lombok
public class DetallePedido {

    // ==========================================
    // LLAVE PRIMARIA
    // ==========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental en el motor SQL
    @Column(name = "id_detalle")
    private Integer idDetalle;

    // ==========================================
    // RELACIONES DE BASE DE DATOS (LLAVES FORÁNEAS)
    // ==========================================

    /**
     * Relación: Muchos detalles pertenecen a UN solo pedido (Many-To-One).
     * fetch = FetchType.LAZY activa la "Carga Perezosa": El pedido asociado no se traerá 
     * de la base de datos a menos que explícitamente se llame a detalle.getPedido(). 
     * Esto ahorra mucha memoria y optimiza las consultas SQL.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false) // Nombre de la columna FK en la base de datos
    @ToString.Exclude // Previene bucles de recursividad infinita en logs o depuración
    private Pedido pedido;

    /**
     * Relación: Muchos detalles pueden referenciar a UN solo producto del catálogo.
     * Al igual que la anterior, utiliza FetchType.LAZY para evitar sobrecargar la memoria 
     * con datos de productos que tal vez no necesitemos renderizar inmediatamente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false) // Nombre de la columna FK en la base de datos
    @ToString.Exclude // Evita errores de desbordamiento de pila (StackOverflowError)
    private Producto producto;

    // ==========================================
    // ATRIBUTOS DE NEGOCIO
    // ==========================================

    @Column(nullable = false)
    private Integer cantidad; // Cantidad de unidades compradas de este producto específico

    /**
     * Precio de la planta o producto en el momento exacto de la compra.
     * precision = 10, scale = 2 significa que el número puede tener hasta 10 dígitos en total,
     * de los cuales exactamente 2 serán decimales (ej. 99999999.99).
     */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /**
     * El costo total de esta línea de detalle (cantidad multiplicada por precio_unitario).
     * Mantiene la misma escala decimal fija para garantizar exactitud en las operaciones.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}