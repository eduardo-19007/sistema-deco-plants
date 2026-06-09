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


//API REST para la gestión de Productos. sino que devuelve directamente los datos en formato JSON (JavaScript Object Notation).
@RestController 

@RequestMapping("/api/productos") 
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

 
    //Recupera el catálogo completo de productos.
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // 2. LEER UNO POR ID (Método HTTP: GET)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Optional<Producto> producto = productoRepository.findById(id);
        
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    // 3. CREAR (Método HTTP: POST)
    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            @Valid @RequestBody Producto producto
    ) {
        Producto nuevoProducto = productoRepository.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED); 
    }

    // 4. ACTUALIZAR (Método HTTP: PUT)

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Integer id, 
            @Valid @RequestBody Producto productoActualizado
    ) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
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

    // 5. ELIMINAR (Método HTTP: DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}