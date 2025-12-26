package com.masbytes.rbacapi.shared.infrastructure.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/status")
    public Map<String, Object> status() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Map.of(
                    "authenticated", false,
                    "message", "No active session"
            );
        }

        return Map.of(
                "authenticated", true,
                "user", auth.getName(),
                "authorities", auth.getAuthorities()
        );
    }
}
