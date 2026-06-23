package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.dtos.ProductoDTO;
import com.decoplants.sistema_web.models.Categoria;
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.CategoriaRepository;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController 
@RequestMapping("/api/productos") 
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. LEER TODOS LOS ACTIVOS CON PAGINACIÓN (Para el Catálogo)
    @GetMapping
    public ResponseEntity<Page<Producto>> listarPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "idProducto") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        // Determina la dirección del ordenamiento
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        
        // Construye el objeto de paginación
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Ejecuta la consulta paginada solo para los productos activos
        Page<Producto> productosPaginados = productoRepository.findByEstadoTrue(pageable);
        
        return ResponseEntity.ok(productosPaginados);
    }

    // 2. LEER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. CREAR UTILIZANDO DTO (Protección contra Mass Assignment)
    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(productoDTO.getIdCategoria());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La categoría especificada no existe en el sistema.");
        }

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(productoDTO.getNombre());
        nuevoProducto.setDescripcion(productoDTO.getDescripcion());
        nuevoProducto.setPrecio(productoDTO.getPrecio());
        nuevoProducto.setStock(productoDTO.getStock());
        nuevoProducto.setImagen(productoDTO.getImagen());
        nuevoProducto.setEstado(productoDTO.getEstado());
        nuevoProducto.setCategoria(categoriaOpt.get());

        Producto guardado = productoRepository.save(nuevoProducto);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED); 
    }

    // 4. ACTUALIZAR UTILIZANDO DTO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Integer id, 
            @Valid @RequestBody ProductoDTO productoDTO
    ) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Categoria> categoriaOpt = categoriaRepository.findById(productoDTO.getIdCategoria());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La categoría especificada no existe en el sistema.");
        }

        Producto producto = productoExistente.get();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagen(productoDTO.getImagen());
        producto.setEstado(productoDTO.getEstado());
        producto.setCategoria(categoriaOpt.get());

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // 5. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build();
    }
}