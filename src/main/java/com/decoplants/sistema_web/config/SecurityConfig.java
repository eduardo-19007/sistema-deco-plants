package com.decoplants.sistema_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // =========================================================
            // 1. CONFIGURACIÓN CSRF (PROTECCIÓN CONTRA ATAQUES EXTERNOS)
            // =========================================================
            // Por defecto, Spring bloquea peticiones POST/PUT/DELETE externas (como Postman).
            // Con esta línea, le decimos que ignore esa regla SOLO para las rutas de nuestra API REST.
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            
            // =========================================================
            // 2. CONFIGURACIÓN DE RUTAS Y PERMISOS
            // =========================================================
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: Accesibles para cualquier visitante sin iniciar sesión.
                // Se incluyó "/api/**" para que Postman y otros clientes externos puedan interactuar libremente.
                .requestMatchers("/", "/registrar-pedido", "/registrar-incidencia", "/css/**", "/img/**", "/js/**", "/api/**").permitAll()
                
                // Rutas privadas: Solo los usuarios con el rol "ADMIN" pueden acceder a las URLs del panel.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Cualquier otra ruta no especificada arriba requerirá autenticación por defecto.
                .anyRequest().authenticated()
            )
            
            // =========================================================
            // 3. CONFIGURACIÓN DEL FORMULARIO DE LOGIN
            // =========================================================
            .formLogin(login -> login
                .loginPage("/login") // Le indicamos a Spring que use nuestra vista HTML personalizada.
                .defaultSuccessUrl("/admin/pedidos", true) // Redirección al panel si las credenciales son correctas.
                .permitAll() // Permitimos que todos puedan ver la página de login.
            )
            
            // =========================================================
            // 4. CONFIGURACIÓN DEL CIERRE DE SESIÓN
            // =========================================================
            .logout(logout -> logout
                .logoutSuccessUrl("/") // Redirección automática a la tienda pública al salir.
                .permitAll()
            );
            
        return http.build();
    }

    // =========================================================
    // 5. CONFIGURACIÓN DE USUARIOS (EN MEMORIA)
    // =========================================================
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Creamos un usuario administrador directamente en el código (ideal para esta fase de desarrollo).
        UserDetails admin = User.builder()
            .username("admin") // <--- Usuario para ingresar al panel
            .password(passwordEncoder().encode("admin123")) // <--- Contraseña encriptada por seguridad
            .roles("ADMIN") // Se le asigna el rol definido en las rutas privadas
            .build();
            
        return new InMemoryUserDetailsManager(admin);
    }

    // =========================================================
    // 6. MOTOR DE ENCRIPTACIÓN DE CONTRASEÑAS
    // =========================================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt es el estándar de la industria actual para encriptar contraseñas de forma segura.
        return new BCryptPasswordEncoder();
    }
}