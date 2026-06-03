package com.decoplants.sistema_web.controllers;

import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.ProductoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que es una API REST (devuelve JSON, no HTML)
@RequestMapping("/api/productos") // La ruta base para todas estas operaciones
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. LEER TODOS (GET)
    // Ruta: http://localhost:8080/api/productos
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // 2. LEER UNO POR ID (GET)
    // Ruta: http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build(); // Devuelve un error 404 si no existe
        }
    }

    // 3. CREAR (POST)
    // Usamos @Valid para que Spring revise las reglas que pusimos en el modelo antes de guardar
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoRepository.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED); // Devuelve un estado 201 (Creado)
    }

    // 4. ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @Valid @RequestBody Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            // Actualizamos los campos
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            producto.setImagen(productoActualizado.getImagen());
            producto.setEstado(productoActualizado.getEstado());
            producto.setCategoria(productoActualizado.getCategoria());
            
            return ResponseEntity.ok(productoRepository.save(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Estado 204 (Sin contenido, eliminado con éxito)
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}