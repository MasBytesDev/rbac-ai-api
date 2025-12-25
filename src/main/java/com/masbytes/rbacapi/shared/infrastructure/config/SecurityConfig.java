package com.masbytes.rbacapi.shared.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration class. Defines the security filter chain and
 * password encoder beans. Currently configured to disable CSRF and allow all
 * requests for testing purposes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the HTTP security filter chain. - Disables CSRF protection
     * (necessary for POST/PATCH in APIs). - Permits all requests without
     * authentication (temporary for testing).
     *
     * @param http the HttpSecurity configuration object
     * @return the built SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Necesario para POST/PATCH en APIs
                .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite probar sin tokens ni logins por ahora
                );
        return http.build();
    }

    /**
     * Provides a BCrypt password encoder bean. Used to securely hash and verify
     * user passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
