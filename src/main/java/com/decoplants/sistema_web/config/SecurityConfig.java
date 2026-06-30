package com.decoplants.sistema_web.config;

import com.decoplants.sistema_web.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Se deshabilita CSRF para permitir peticiones API fluidas
            .authorizeHttpRequests(auth -> auth
                // RUTAS PÚBLICAS (Vistas y API lectura)
                .requestMatchers("/", "/index.html", "/registrar-pedido", "/registrar-incidencia").permitAll()
                .requestMatchers("/css/**", "/img/**", "/js/**", "/recursos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll() 
                .requestMatchers("/api/auth/login").permitAll() // Endpoint para obtener el JWT

                // RUTAS PROTEGIDAS PARA EL PANEL WEB (Thymeleaf)
                .requestMatchers("/admin/productos/**").hasRole("ADMIN")
                .requestMatchers("/admin/pedidos/**", "/admin/incidencias/**").hasAnyRole("ADMIN", "VENDEDOR")

                // RUTAS PROTEGIDAS API REST (Solo Modificación con JWT)
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    // Obtenemos los roles del usuario que acaba de iniciar sesión
                    boolean isAdminOrVendedor = authentication.getAuthorities().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_VENDEDOR"));
                    
                    // Si es administrador o vendedor, va al panel de control
                    if (isAdminOrVendedor) {
                        response.sendRedirect("/admin/pedidos");
                    } else {
                        // Si es un CLIENTE normal, lo mandamos a la página principal (catálogo)
                        response.sendRedirect("/"); 
                    }
                })
                .permitAll()
            ) // <-- ¡Corregido! Solo un paréntesis de cierre aquí
            .logout(logout -> logout
                .logoutSuccessUrl("/") 
                .permitAll()
            )
            // Aquí inyectamos nuestro filtro JWT antes del filtro estándar de Spring
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Exponemos el AuthenticationManager para usarlo en nuestro AuthRestController
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}