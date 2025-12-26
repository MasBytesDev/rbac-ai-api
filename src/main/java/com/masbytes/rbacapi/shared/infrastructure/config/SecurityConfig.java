package com.masbytes.rbacapi.shared.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.shared.infrastructure.security.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserDetailsService userDetailsService;

    public SecurityConfig(AppUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // APIs REST no necesitan CSRF
            .csrf(AbstractHttpConfigurer::disable)

            // Autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()   // login/logout públicos
                .requestMatchers("/public/**").permitAll()        // otros endpoints públicos
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )

            // Configuración de login
            .formLogin(form -> form
                .loginProcessingUrl("/api/v1/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    var result = Map.of(
                        "message", "Login successful",
                        "user", authentication.getName()
                    );
                    new ObjectMapper().writeValue(response.getWriter(), result);
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    var result = Map.of(
                        "error", "Invalid credentials"
                    );
                    new ObjectMapper().writeValue(response.getWriter(), result);
                })
                .permitAll()
            )

            // Configuración de logout
            .logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    var result = Map.of(
                        "message", "Logout successful"
                    );
                    new ObjectMapper().writeValue(response.getWriter(), result);
                })
            )

            // Integración con tu servicio de usuarios
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
