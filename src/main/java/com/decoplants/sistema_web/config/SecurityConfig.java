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
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas públicas (Cualquiera puede entrar)
                .requestMatchers("/", "/registrar-pedido", "/registrar-incidencia", "/css/**", "/img/**", "/js/**").permitAll()
                // 2. Rutas privadas (Solo el rol ADMIN puede entrar)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login") // Le decimos que usaremos nuestro propio HTML de login
                .defaultSuccessUrl("/admin/pedidos", true) // Si el login es exitoso, lo enviamos al panel
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // Al cerrar sesión, lo enviamos a la tienda
                .permitAll()
            );
            
        return http.build();
    }

    // Creamos un usuario administrador en memoria (Ideal para empezar)
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin") // <--- Tu usuario
            .password(passwordEncoder().encode("admin123")) // <--- Tu contraseña encriptada
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }

    // Motor de encriptación de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}