package com.decoplants.sistema_web.config;

// Importaciones del núcleo de Spring y Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Importaciones para la gestión de usuarios y contraseñas
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de Configuración Global de Seguridad.
 * @Configuration indica que esta clase provee "Beans" (objetos gestionados por Spring) al contexto de la aplicación.
 * @EnableWebSecurity activa los filtros de seguridad web que interceptarán todas las peticiones HTTP entrantes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad (Security Filter Chain).
     * Actúa como un muro de contención (similar a un firewall de aplicación) 
     * que evalúa cada paquete HTTP antes de que alcance a tus Controladores.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // =========================================================
            // 1. CONFIGURACIÓN CSRF (Cross-Site Request Forgery)
            // =========================================================
            // CSRF es una vulnerabilidad donde un sitio malicioso engaña al navegador 
            // para enviar peticiones no autorizadas a nuestro servidor.
            // Spring lo bloquea por defecto. Aquí desactivamos esa protección EXCLUSIVAMENTE 
            // para las rutas "/api/**" porque los clientes REST (como Postman o un frontend en React) 
            // no usan sesiones de navegador, sino que son "Stateless" (sin estado).
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            
            // =========================================================
            // 2. CONFIGURACIÓN DE RUTAS Y CONTROL DE ACCESO (ACL)
            // =========================================================
            .authorizeHttpRequests(auth -> auth
                // Lista Blanca (Whitelist): Rutas públicas sin restricción.
                // Incluye recursos estáticos (CSS, JS, IMG) y los endpoints públicos (Home, Registrar Pedido, API).
                .requestMatchers("/", "/registrar-pedido", "/registrar-incidencia", "/css/**", "/img/**", "/js/**", "/api/**", "/recursos/**").permitAll()
                
                // Restricción por Roles: Cualquier URL que empiece con "/admin/" 
                // requerirá estrictamente que el usuario autenticado posea el rol "ADMIN".
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Regla por Defecto: Si el programador olvida mapear una ruta nueva en el futuro, 
                // el sistema la bloqueará por defecto, exigiendo autenticación. (Principio de Privilegio Mínimo).
                .anyRequest().authenticated()
            )
            
            // =========================================================
            // 3. CONFIGURACIÓN DEL FORMULARIO DE LOGIN (Autenticación)
            // =========================================================
            .formLogin(login -> login
                // Redirige al LoginController que creamos anteriormente.
                .loginPage("/login") 
                // true = Fuerza la redirección al panel de pedidos siempre que el login sea exitoso,
                // evitando que el usuario sea devuelto a una página aleatoria.
                .defaultSuccessUrl("/admin/pedidos", true) 
                .permitAll()
            )
            
            // =========================================================
            // 4. CONFIGURACIÓN DEL CIERRE DE SESIÓN (Logout)
            // =========================================================
            .logout(logout -> logout
                // Invalida la sesión actual en el servidor y devuelve al usuario a la vista pública.
                .logoutSuccessUrl("/") 
                .permitAll()
            );
            
        return http.build();
    }

    // =========================================================
    // 5. PROVEEDOR DE IDENTIDAD (EN MEMORIA)
    // =========================================================
    /**
     * Configura un almacén de usuarios temporal.
     * En lugar de consultar una tabla de base de datos relacional, 
     * el usuario administrador se carga directamente en la memoria RAM del servidor al iniciar.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            // La contraseña en texto plano ("admin123") se pasa por la función de hash 
            // ANTES de guardarse en memoria, cumpliendo los estándares de criptografía.
            .password(passwordEncoder().encode("admin123")) 
            .roles("ADMIN")
            .build();
            
        return new InMemoryUserDetailsManager(admin);
    }

    // =========================================================
    // 6. MOTOR DE CRIPTOGRAFÍA (Hashing)
    // =========================================================
    /**
     * Define el algoritmo criptográfico para proteger las contraseñas.
     * BCrypt no solo encripta, sino que aplica un "Salt" (datos aleatorios adicionales) 
     * a cada contraseña para neutralizar ataques de fuerza bruta o de tablas arcoíris.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}