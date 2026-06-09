package com.decoplants.sistema_web.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEDIDO")
@Data 
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id_pedido")
    private Integer idPedido;

    
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

    @Column(name = "notas_pedido", columnDefinition = "VARCHAR(MAX)")
    private String notasPedido;


    @Column(name = "total_pedido", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPedido;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(nullable = false, length = 30)
    private String estado;


    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude 
    private List<DetallePedido> detalles = new ArrayList<>();

    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle); 
        detalle.setPedido(this); 
    }
}