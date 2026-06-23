package com.decoplants.sistema_web.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {

    private Integer idProducto; // Útil para los procesos de actualización (PUT)

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debe ingresar un precio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "Debe ingresar el stock inicial")
    @Min(value = 0, message = "El stock no puede ser menor a 0")
    private Integer stock;

    private String imagen;
    
    private Boolean estado = true;

    @NotNull(message = "Debe seleccionar una categoría")
    private Integer idCategoria; // Transferimos solo el ID numérico, no la entidad completa
}