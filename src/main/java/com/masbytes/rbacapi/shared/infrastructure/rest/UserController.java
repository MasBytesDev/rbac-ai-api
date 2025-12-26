package com.masbytes.rbacapi.shared.infrastructure.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "User endpoint OK");
    }
}
