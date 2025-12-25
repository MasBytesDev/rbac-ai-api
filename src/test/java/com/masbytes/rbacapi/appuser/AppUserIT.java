package com.masbytes.rbacapi.appuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.appuser.domain.dto.CreateAppUserRequest;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // IMPORTANTE: Hará Rollback en tu Postgres real tras cada test
@SuppressWarnings("unused")
class AppUserIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository repository;

    @Test
    @DisplayName("Full Integration: Create User in PostgreSQL and verify BCrypt hash")
    void createUser_PostgresIntegrationFlow() throws Exception {
        // Arrange
        String email = "dev.test@masbytes.com";
        CreateAppUserRequest request = new CreateAppUserRequest(
            "Admin Test", email, "ComplexPass123!"
        );

        // Act
        mockMvc.perform(post("/api/v1/app-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email));

        // Assert: Validación directa en el Postgres de verdad
        var savedUser = repository.findByEmail(email);
        assertTrue(savedUser.isPresent(), "User should be persisted in PostgreSQL");
        
        // Verificamos que el hashing de BCrypt se haya aplicado correctamente antes de persistir
        assertTrue(savedUser.get().getPasswordHash().startsWith("$2a$"), 
            "Password should be hashed with BCrypt in the database");
    }
}