package com.decoplants.sistema_web.controllers;

// Importaciones del modelo y repositorio
import com.decoplants.sistema_web.models.Producto;
import com.decoplants.sistema_web.repositories.ProductoRepository;

// Validación de datos
import jakarta.validation.Valid;

// Importaciones de Spring Boot para crear APIs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Manejo de colecciones y valores opcionales
import java.util.List;
import java.util.Optional;

/**
 * API REST para la gestión de Productos.
 * @RestController es la combinación de @Controller + @ResponseBody. 
 * A diferencia de los controladores anteriores, este NO devuelve vistas HTML, 
 * sino que devuelve directamente los datos en formato JSON (JavaScript Object Notation).
 */
@RestController 
// @RequestMapping define el prefijo de la URL para todos los métodos de esta clase.
// Es una convención usar "/api/..." para diferenciar estas rutas de las páginas web normales.
@RequestMapping("/api/productos") 
public class ProductoRestController {

    @Autowired
    private ProductoRepository productoRepository;

    // ==========================================
    // 1. LEER TODOS (Método HTTP: GET)
    // ==========================================
    /**
     * Recupera el catálogo completo de productos.
     * Ruta final: GET http://localhost:8080/api/productos
     */
    @GetMapping
    public List<Producto> listarTodos() {
        // Ejecuta un SELECT * en la base de datos y Spring lo convierte automáticamente a JSON.
        return productoRepository.findAll();
    }

    // ==========================================
    // 2. LEER UNO POR ID (Método HTTP: GET)
    // ==========================================
    /**
     * Busca un producto específico usando su identificador.
     * Ruta final: GET http://localhost:8080/api/productos/{id}
     * Usamos ResponseEntity para poder manipular los códigos de estado HTTP (200, 404, etc.).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        // Optional nos ayuda a prevenir errores si el ID ingresado no existe en la BD.
        Optional<Producto> producto = productoRepository.findById(id);
        
        if (producto.isPresent()) {
            // Retorna un estado 200 (OK) junto con los datos del producto.
            return ResponseEntity.ok(producto.get());
        } else {
            // Retorna un estado 404 (Not Found) vacío, indicando que no existe.
            return ResponseEntity.notFound().build(); 
        }
    }

    // ==========================================
    // 3. CREAR (Método HTTP: POST)
    // ==========================================
    /**
     * Inserta un nuevo producto en la base de datos.
     * Ruta final: POST http://localhost:8080/api/productos
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            // @Valid fuerza a Spring a revisar las validaciones (ej. @NotNull, @Size) definidas en la clase Producto.
            // @RequestBody toma el JSON que envía el cliente (ej. Postman o Frontend) y lo convierte en el objeto Java 'Producto'.
            @Valid @RequestBody Producto producto
    ) {
        // Guarda el nuevo producto en la base de datos.
        Producto nuevoProducto = productoRepository.save(producto);
        
        // Retorna el producto creado junto con el código 201 (Created), que es el estándar API para creaciones exitosas.
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED); 
    }

    // ==========================================
    // 4. ACTUALIZAR (Método HTTP: PUT)
    // ==========================================
    /**
     * Sobrescribe los datos de un producto existente.
     * Ruta final: PUT http://localhost:8080/api/productos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Integer id, 
            @Valid @RequestBody Producto productoActualizado
    ) {
        // 1. Verificamos si el producto a editar realmente existe.
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isPresent()) {
            // 2. Extraemos el objeto original de la base de datos.
            Producto producto = productoExistente.get();
            
            // 3. Sobrescribimos sus atributos con los datos nuevos que llegaron en el JSON.
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            producto.setImagen(productoActualizado.getImagen());
            producto.setEstado(productoActualizado.getEstado());
            producto.setCategoria(productoActualizado.getCategoria());
            
            // 4. Guardamos los cambios y devolvemos un estado 200 (OK).
            return ResponseEntity.ok(productoRepository.save(producto));
        } else {
            // Si mandaron un ID falso, devolvemos 404 (Not Found).
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // 5. ELIMINAR (Método HTTP: DELETE)
    // ==========================================
    /**
     * Borra un producto de la base de datos.
     * Ruta final: DELETE http://localhost:8080/api/productos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        // Validamos si existe antes de intentar borrar.
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            
            // El código 204 (No Content) es el estándar API para indicar: 
            // "La acción tuvo éxito, pero no hay datos adicionales que devolverte".
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}