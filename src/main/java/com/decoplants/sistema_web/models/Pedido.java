package com.decoplants.sistema_web.models;

// Importaciones de Jakarta Persistence API (JPA) para el mapeo relacional
import jakarta.persistence.*;

// Importaciones de Lombok para la automatización de Getters, Setters y utilitarios
import lombok.Data;
import lombok.ToString;

// Tipos de datos para alta precisión monetaria y manejo temporal
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de la entidad Pedido.
 * Actúa como la "Cabecera" de la compra, almacenando los datos globales de la transacción,
 * del cliente y del envío. Mapea la tabla "PEDIDO" en la base de datos.
 */
@Entity
@Table(name = "PEDIDO")
@Data // Lombok genera automáticamente constructores, getters, setters, equals y hashCode
public class Pedido {

    // ==========================================
    // LLAVE PRIMARIA
    // ==========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Identificador autoincremental en el motor SQL
    @Column(name = "id_pedido")
    private Integer idPedido;

    // ==========================================
    // DATOS DE FACTURACIÓN Y ENVÍO
    // ==========================================
    
    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(name = "telefono_cliente", nullable = false, length = 20)
    private String telefonoCliente;

    @Column(name = "direccion_envio", nullable = false, length = 255)
    private String direccionEnvio;

    // Almacena el medio de pago seleccionado (ej. "Transferencia", "Tarjeta", "Yape")
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    // Define la forma de entrega (ej. "Delivery", "Recojo en Tienda")
    @Column(name = "modalidad_entrega", nullable = false, length = 50)
    private String modalidadEntrega;

    /**
     * Indicaciones opcionales dadas por el comprador (ej. "Dejar en portería").
     * columnDefinition = "VARCHAR(MAX)" garantiza que no haya restricciones de longitud
     * en sistemas de bases de datos relacionales robustos.
     */
    @Column(name = "notas_pedido", columnDefinition = "VARCHAR(MAX)")
    private String notasPedido;

    // ==========================================
    // METADATOS FINANCIEROS Y DE SEGUIMIENTO
    // ==========================================

    // Monto total acumulado de la compra (suma de los subtotales de los detalles).
    // Mantiene una precisión fija de 10 dígitos y 2 decimales para evitar pérdidas contables.
    @Column(name = "total_pedido", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPedido;

    // Almacena el momento exacto (fecha y hora) en que el cliente confirmó la orden.
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    // Controla el flujo de trabajo del pedido (ej. "Pendiente", "En Ruta", "Entregado")
    @Column(nullable = false, length = 30)
    private String estado;

    // ==========================================
    // RELACIÓN BIDIRECCIONAL COMPUESTA
    // ==========================================
    /**
     * Relación One-To-Many (Un pedido contiene muchas líneas de detalle).
     * * - mappedBy = "pedido": Indica que la FK está en la clase DetallePedido (dueño de la relación).
     * - cascade = CascadeType.ALL: Operación en cascada total. Si guardamos, actualizamos o eliminamos 
     * un Pedido, todas sus líneas de detalle se guardarán, actualizarán o eliminarán automáticamente 
     * en la base de datos dentro de la misma transacción.
     * - orphanRemoval = true: Si removemos un detalle de esta lista de Java, JPA se encargará de 
     * ejecutar un DELETE físico de esa fila en la base de datos, evitando registros huérfanos.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Rompe la recursividad infinita con DetallePedido al serializar a cadena de texto
    private List<DetallePedido> detalles = new ArrayList<>();

    // ==========================================
    // MÉTODOS DE AYUDA (HELPER METHODS)
    // ==========================================
    /**
     * Sincroniza la relación bidireccional de manera segura.
     * Al agregar un ítem de detalle a la lista del pedido, este método se asegura de 
     * asignarle la referencia de este pedido al detalle en la misma operación de memoria.
     * Vital para que la persistencia de Hibernate no falle por referencias nulas.
     * * @param detalle Objeto DetallePedido que contiene el producto y cantidad.
     */
    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle); // Agrega el detalle a la colección del Pedido (Extremo One)
        detalle.setPedido(this); // Inyecta este pedido dentro del objeto detalle (Extremo Many)
    }
}