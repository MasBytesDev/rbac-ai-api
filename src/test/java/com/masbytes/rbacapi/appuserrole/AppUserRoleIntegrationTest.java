package com.masbytes.rbacapi.appuserrole;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import com.masbytes.rbacapi.appuserrole.domain.dto.AssignRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.dto.UpdateUserRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.repository.AppUserRoleRepository;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

//  import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("unused")
@Transactional // Limpia la base de datos después de cada test
class AppUserRoleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AppUserRoleRepository userRoleRepository;

    private AppUser savedUser;
    private Role savedRoleAdmin;
    private Role savedRoleEditor;

    @BeforeEach
    void setUp() {
        // 1. SOLO limpiamos la tabla de la relación que nos compete
        userRoleRepository.deleteAll();

        // 2. Usamos datos únicos para evitar colisiones con lo que ya exista en la BD
        String uniqueEmail = "integration_" + UUID.randomUUID() + "@test.com";

        savedUser = userRepository.save(AppUser.builder()
                .fullname("Integration Test User")
                .email(uniqueEmail)
                .passwordHash("password123")
                .build());

        savedRoleAdmin = roleRepository.save(Role.builder()
                .roleName("ROLE_ADMIN_INT_" + UUID.randomUUID())
                .roleDescription("Administrator")
                .build());

        savedRoleEditor = roleRepository.save(Role.builder()
                .roleName("ROLE_EDITOR_INT_" + UUID.randomUUID())
                .roleDescription("Editor")
                .build());
    }

    @Test
    @DisplayName("Integration: Should assign and then update a role in the database")
    void shouldAssignAndUpdateRole() throws Exception {
        // 1. ASIGNAR (POST)
        var assignRequest = new AssignRoleRequest(savedUser.getPublicId(), savedRoleAdmin.getPublicId());

        mockMvc.perform(post("/api/v1/app-users/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isCreated())
                // CAMBIO AQUÍ: Usamos la variable en lugar de texto fijo
                .andExpect(jsonPath("$.roleName").value(savedRoleAdmin.getRoleName()));

        // ... (parte intermedia igual)
        // 2. ACTUALIZAR / SUSTITUIR (PATCH)
        var updateRequest = new UpdateUserRoleRequest(savedRoleAdmin.getPublicId(), savedRoleEditor.getPublicId());

        mockMvc.perform(patch("/api/v1/app-users/{id}/roles", savedUser.getPublicId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                // CAMBIO AQUÍ: Usamos la variable del editor
                .andExpect(jsonPath("$.roleName").value(savedRoleEditor.getRoleName()));

        // ... (verificaciones de BD iguales)
    }
}
