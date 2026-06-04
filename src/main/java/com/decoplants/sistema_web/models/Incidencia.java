package com.decoplants.sistema_web.models;

// Importaciones de Jakarta Persistence API (JPA) para el mapeo a tablas
import jakarta.persistence.*;

// Lombok para evitar escribir código repetitivo (getters, setters, etc.)
import lombok.Data;

// Importación para manejar la fecha y hora exacta del sistema
import java.time.LocalDateTime;

/**
 * Modelo de la entidad Incidencia.
 * Representa un ticket de soporte, reclamo o queja de un cliente.
 * Mapea la tabla "INCIDENCIA" en la base de datos.
 */
@Entity
@Table(name = "INCIDENCIA")
@Data 
public class Incidencia {
    
    // ==========================================
    // LLAVE PRIMARIA
    // ==========================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El ID se genera automáticamente (Autoincremental)
    @Column(name = "id_incidencia")
    private Integer idIncidencia;

    // ==========================================
    // DATOS DE CONTACTO DEL CLIENTE
    // ==========================================
    
    // nullable = false hace que el campo sea obligatorio (NOT NULL).
    // length = 150 optimiza el espacio en disco, reservando solo 150 caracteres.
    @Column(name = "nombre_cliente", nullable = false, length = 150)
    private String nombreCliente;

    @Column(nullable = false, length = 20)
    private String telefono;

    // ==========================================
    // DETALLES DEL RECLAMO
    // ==========================================
    
    // Clasificación del ticket (ej. "Reclamo", "Garantía", "Consulta")
    @Column(nullable = false, length = 50)
    private String tipo;

    /**
     * Descripción detallada del problema.
     * columnDefinition = "VARCHAR(MAX)" es una instrucción directa al motor de base de datos.
     * A diferencia del límite normal de 255 caracteres de un String en JPA, esto permite 
     * que el cliente escriba textos muy extensos sin que la base de datos corte el mensaje.
     */
    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    // ==========================================
    // METADATOS DE AUDITORÍA (Controlados por el servidor)
    // ==========================================
    
    // Registra la marca de tiempo (timestamp) exacta en la que se creó el ticket.
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // Estado del ticket para seguimiento (ej. "Abierto", "En Revisión", "Resuelto")
    @Column(nullable = false, length = 30)
    private String estado;
}