package com.masbytes.rbacapi.auth.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // Spring Security intercepta este endpoint automáticamente
        // No necesitas implementar lógica aquí, el framework maneja la autenticación
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Spring Security también intercepta /logout
        return ResponseEntity.ok("Logout successful");
    }
}
